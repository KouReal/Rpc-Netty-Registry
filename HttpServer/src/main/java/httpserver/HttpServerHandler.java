package httpserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptionutils.ParseRequestException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import protocolutils.RpcRequest;

@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	public static final Logger LOGGER = LoggerFactory.getLogger(HttpServerHandler.class);

	
    private static ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1000), new NamedThreadFactory("httpserver-pool", false)); ;



	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg){
		LOGGER.info("get fullhttprequest\n :{}",msg);
		String ssotid = HttpMessageUtil.checkssoredirect(msg);
		 	if(ssotid!=null){
		 		/*response.headers().set("Location", HttpMessageUtil.token2urlcache.get(ssotid));
				LOGGER.info("重定向2->{}",HttpMessageUtil.token2urlcache.get(ssotid));*/
				HttpMessageUtil.tokenidcache.remove(ssotid);
				Cookie cookie=new DefaultCookie("tokenid",ssotid);
				FullHttpResponse response = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, "登录成功,设置tokenid到cookie", cookie);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
				
			}else{
				try {
					RpcRequest rpcRequest = HttpMessageUtil.buildrpcrequest(msg);
					LOGGER.info("build rpcrequest:{}",rpcRequest);
					threadPool.execute(new HttpTask(ctx, rpcRequest));
//					threadPool.execute(new microtask(ctx, rpcRequest, "http://127.0.0.1:8081"+msg.uri()));
				} catch (ParseRequestException e) {
					FullHttpResponse response = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, e.getMessage(), null);
					ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
					
				}
				
			}
			

		
		
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("api caught exception", cause);
        ctx.channel().close();
    }
	
	
	
   
}

/*//无限重定向task
class microtask implements Runnable{
	private ChannelHandlerContext ctx;
	private RpcRequest rpcRequest;
	private String url;
	private Logger log = LoggerFactory.getLogger(microtask.class);
	
	public microtask(ChannelHandlerContext ctx,RpcRequest rpcRequest,String url) {
		this.ctx = ctx;
		this.rpcRequest = rpcRequest;
		this.url = url;
	}
	@Override
	public void run() {
		
		//发送请求
		//收到回复
		//经过判断，是个token回复，说明登录成功，需要设置客户为token使用者
		//假如tokenid为834982，那么重定向到/bank/834982
		String tokenid = UUID.randomUUID().toString();
		HttpMessageUtil.token2urlcache.put(tokenid,url);
		//httpMessageUtil.getTokenidcache().add(tokenid);
		FullHttpResponse response = HttpMessageUtil.buildhttpresponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND, "正在重定向1", null);
		response.headers().set("Location", "http://127.0.0.1:8081/bank/"+tokenid);
		log.info("第一次重定向");
		log.info("response:{}",response);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);;
		
		
	}
	
}*/
