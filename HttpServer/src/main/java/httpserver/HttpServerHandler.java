
package httpserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import exceptionutils.ParseRequestException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	public final Logger LOGGER = LoggerFactory.getLogger(HttpServerHandler.class);
	@SuppressWarnings("deprecation")
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg){
		try{
			try {
				buildrpcrequest(msg);
			} catch (ParseRequestException e) {
				FullHttpResponse response = buildhttpresponse(HttpVersion.HTTP_1_0, HttpResponseStatus.BAD_REQUEST, e.getMessage(), null);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
				
			}
			
		}finally{
			ctx.close();
		}
		

		//redirect
		
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		

/*		Cookie cookie2=new DefaultCookie("captchaID","123");
		response.headers().set(HttpHeaders.Names.SET_COOKIE, ClientCookieEncoder.STRICT.encode(cookie2));

		//response.headers().set("Cookie","123");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);*/
	}
	public static FullHttpResponse buildhttpresponse(
			HttpVersion version,
			HttpResponseStatus status, 
			String message,
			Cookie cookie){
			FullHttpResponse response = new DefaultFullHttpResponse(version,status);
			if(cookie!=null){
				response.headers().set("Set-Cookie",ClientCookieEncoder.STRICT.encode(cookie));
			}
			ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
			response.content().writeBytes(buffer);
			return response;
		
	}
	
	public void buildrpcrequest(FullHttpRequest msg) throws ParseRequestException{
		

		LOGGER.debug("httprequest :{}",msg);
		
		HttpHeaders headers = msg.headers();
		LOGGER.info("headers : {}",headers);
		String cookie = headers.get("Cookie");
		LOGGER.debug("cookie:{} ",cookie);
		
		if(msg.method()==HttpMethod.POST){
			Map<String, Object> postparams = parsepostparams(msg);
			String jsonString = JSON.toJSONString(postparams);
			LOGGER.info("post params : {}",jsonString);
		}else if(msg.method()==HttpMethod.GET){
			parsegetparams(msg);
		}else{
			throw new ParseRequestException("不支持http请求方式："+msg.method().toString());
		}
		


		
	}
	public Map<String, String> parsegetparams(FullHttpRequest request) throws ParseRequestException{
		String uri = request.uri();
		LOGGER.debug("parsegetparams:uri:{}", uri);
		int queryindex = uri.indexOf("?");
		if(queryindex>=0){
			Map<String, String> parammap = new HashMap<>();
			String quetystr = uri.substring(queryindex+1, uri.length());
			LOGGER.debug("get?paramastr:{}",quetystr);
			String[] parmas = quetystr.split("&");
			LOGGER.debug("splited & params:{}", Arrays.toString(parmas));
			for(String par : parmas){
				String[] kvs = par.split("=");
				LOGGER.debug("splited = params:{}", Arrays.toString(kvs));
				if(kvs.length!=2){
					throw new ParseRequestException("get方法的请求参数有问题！");
				}
				parammap.put(kvs[0], kvs[1]);
			}
			return parammap;
		}else{
			return null;
		}
	}
	public Map<String, Object> parsepostparams(FullHttpRequest request) {
	    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
	    List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
	    Map<String, Object> params = new HashMap<>();

	    for (InterfaceHttpData data : httpPostData) {
	        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
	            MemoryAttribute attribute = (MemoryAttribute) data;
	            params.put(attribute.getName(), attribute.getValue());
	        }
	    }
	    return params;
	}
   
}
