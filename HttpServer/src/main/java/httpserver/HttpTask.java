package httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import RpcClient.RpcProxy;
import asyncutils.ResultFuture;
import exceptionutils.RpcErrorException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import protocolutils.RpcRequest;
import protocolutils.RpcResponse;
import springutils.SpringContextStatic;

public class HttpTask implements Runnable{

	private Logger logger = LoggerFactory.getLogger(HttpTask.class);
    private ChannelHandlerContext ctx;
    private RpcRequest rpcRequest;
    private ResultFuture<RpcResponse> future = null;
    
    
    private RpcProxy rpcProxy = null;
    
    

    public HttpTask(ChannelHandlerContext ctx, RpcRequest request) {
        this.ctx = ctx;
        this.rpcRequest = request;
        this.rpcProxy = (RpcProxy) SpringContextStatic.getBean("rpcProxy");
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.info("HttpTask发送rpcrequest:{}",rpcRequest);
			future = rpcProxy.call(rpcRequest);
		} catch (RpcErrorException e) {
			// TODO Auto-generated catch block
			logger.info("HttpTask调用RpcProxy出现错误:{}",e.getMessage());
			e.printStackTrace();
		}
		RpcResponse rpcResponse = future.get();
		logger.info("HttpTask接收到rpcresponse:{}",rpcResponse);
		if(rpcResponse==null){
			logger.error("httptask 调用服务超时");
		}
		
		handleresponse(rpcResponse);
	}
	public void handleresponse(RpcResponse rpcResponse){
		if(rpcResponse==null){
			FullHttpResponse httpResponse = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, "调用服务超时", null);
			ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
			return ;
		}
		byte responsetype = rpcResponse.getResponsetype();
		String content = rpcResponse.getContent();
		FullHttpResponse httpResponse = null;
		if(responsetype==RpcResponse.RESOURCE){
			logger.info("向http客户端发送content:{}",content);
			httpResponse = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content, null);
			
		}else if(responsetype==RpcResponse.FAILURE){
			String errormsg = rpcResponse.getErrormsg();
			logger.info("向http客户端发送rpcresponse类型为FAILURE:{}",errormsg);
			httpResponse = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, errormsg+content, null);
		}else if(responsetype==RpcResponse.REDIRECT){
			logger.info("rpcresponse重定向要求");
			//暂时不存在
		}else if(responsetype==RpcResponse.TOKEN){
			JSONObject jso = JSONObject.parseObject(content);
			String tokenid = (String) jso.get("tokenid");
			if(tokenid==null){
				logger.info("收到了token_response,但是tokenid是null");
				httpResponse = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, "收到了token_response,但是tokenid是null", null);
			}else{
//				Cookie cookie=new DefaultCookie("tokenid",tokenid);
				httpResponse = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND, "正在重定向到bank根目录", null);
				httpResponse.headers().set("Location", "http://"+HttpMessageUtil.httpaddr+"/bank/"+tokenid);
				HttpMessageUtil.tokenidcache.add(tokenid);
			}
		}else{
			String error = "响应类型无法解析";
			logger.info("响应类型无法解析：{}",responsetype);
			httpResponse = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY, error, null);
			
		}
		
		ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
	}
}
