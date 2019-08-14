package httpserver;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import RpcClient.RpcProxy;
import RpcTrans.RpcFuture;
import exceptionutils.RpcErrorException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

/*public class HttpTask implements Runnable{

	private Logger logger = LoggerFactory.getLogger(HttpTask.class);
    private ChannelHandlerContext ctx;
    private RpcRequest rpcRequest;
    private RpcFuture future = null;
    
    private static final int waittimemills = 10000;
    
   // @Autowired
    private RpcProxy rpcProxy;
    
   // @Autowired
    private HttpMessageUtil httpMessageUtil;

    public HttpTask(ChannelHandlerContext ctx, RpcRequest request) {
        this.ctx = ctx;
        this.rpcRequest = request;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			future = rpcProxy.call(rpcRequest);
		} catch (RpcErrorException e) {
			// TODO Auto-generated catch block
			logger.info("HttpTask调用RpcProxy出现错误");
			e.printStackTrace();
		}
		try {
			RpcResponse rpcResponse = future.get(waittimemills, TimeUnit.MILLISECONDS);
			logger.info("HttpTask接收到RpcResponse:{}",rpcResponse);
			handleresponse(rpcResponse);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			logger.info("HttpTask等待RpcFuture超时");
			e.printStackTrace();
		}
	}
	public void handleresponse(RpcResponse rpcResponse){
		byte responsetype = rpcResponse.getResponsetype();
		String content = rpcResponse.getContent();
		FullHttpResponse httpResponse = null;
		if(responsetype==RpcResponse.RESOURCE){
			logger.info("向http客户端发送content:{}",content);
			httpResponse = httpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, content, null);
			
		}else if(responsetype==RpcResponse.FAILURE){
			String errormsg = rpcResponse.getErrormsg();
			logger.info("rpcresponse类型为FAILURE",errormsg);
			httpResponse = httpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_0, HttpResponseStatus.BAD_REQUEST, errormsg, null);
		}else if(responsetype==RpcResponse.REDIRECT){
			logger.info("rpcresponse重定向要求");
			httpResponse = httpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, content, null);
			
		}else if(responsetype==RpcResponse.TOKEN){
			String msgstr = "登录失败";
			if(content!=null){
				msgstr = "登录成功";
			}
			Cookie cookie=new DefaultCookie("tokenid",content);
			httpResponse = httpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, msgstr, cookie);
			
		}else{
			String error = "响应类型无法解析";
			logger.info(error+responsetype);
			httpResponse = httpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_0, HttpResponseStatus.BAD_GATEWAY, error, null);
			
		}
		
		ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
	}
}*/
