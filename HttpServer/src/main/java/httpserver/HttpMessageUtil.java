package httpserver;

import java.util.ArrayList;
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
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import protocolutils.RpcRequest;

public class HttpMessageUtil {
	public static Logger LOGGER = LoggerFactory.getLogger(HttpMessageUtil.class);
	
	public static List<String> tokenidcache = new ArrayList<String>();
//	public static Map<String, String> token2urlcache = new HashMap<String, String>();
	
	public static String httpaddr = null; 
	
	
	public static RpcRequest buildrpcrequest(FullHttpRequest msg) throws ParseRequestException{

		
		
		HttpHeaders headers = msg.headers();
		LOGGER.info("headers : {}",headers);
		String cookie = headers.get("Cookie");
		LOGGER.info("cookie:{} ",cookie);
		String tokenid = null;
		if(cookie!=null&&!cookie.equals("")){
			String[] kv = cookie.split("=");
			if(kv[0]!=null&&kv[0].equals("tokenid")){
				tokenid = kv[1];
			}
			
		}
		LOGGER.info("cookie:tokenid:{}",tokenid);
		String uri = msg.uri();
		int idx = uri.indexOf("?");
		if(idx!=-1){
			uri = uri.split("\\?")[0];
		}
		LOGGER.info("http:uri:{}", uri);
		String[] urisps = uri.split("/");
		LOGGER.info(urisps[1]+" " +urisps[2]+" "+urisps[3]);
		String servicename = urisps[2];
		String methodname = urisps[3];

		
		String paramstr = null;
		String httpmethod = null;
		if(msg.method()==HttpMethod.POST){
			httpmethod="post";
			Map<String, Object> postparams = parsepostparams(msg);
			postparams.put("tokenid", tokenid);
			paramstr = JSON.toJSONString(postparams);
			LOGGER.info("post params : {}",paramstr);
		}else if(msg.method()==HttpMethod.GET){
			httpmethod="get";
			Map<String,String> getparms = parsegetparams(msg);
			getparms.put("tokenid", tokenid);
			paramstr = JSON.toJSONString(getparms);
			LOGGER.info("get params : {}",paramstr);
		}else{
			throw new ParseRequestException("不支持http请求方式："+msg.method().toString());
		}
		/*String requestId = UUID.randomUUID().toString();
		LOGGER.info("new requestid:{}",requestId);*/
		RpcRequest rpcRequest = new RpcRequest(tokenid, servicename, methodname, httpmethod, paramstr);
		return rpcRequest;
	}
	
	public static Map<String, String> parsegetparams(FullHttpRequest request) throws ParseRequestException{
		String uri = request.uri();
		LOGGER.info("parsegetparams:uri:{}", uri);
		int queryindex = uri.indexOf("?");
		if(queryindex>=0){
			Map<String, String> parammap = new HashMap<>();
			String quetystr = uri.substring(queryindex+1, uri.length());
			LOGGER.info("get?paramastr:{}",quetystr);
			String[] parmas = quetystr.split("&");
			LOGGER.info("splited & params:{}", Arrays.toString(parmas));
			for(String par : parmas){
				String[] kvs = par.split("=");
				LOGGER.info("splited = params:{}", Arrays.toString(kvs));
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
	public static Map<String, Object> parsepostparams(FullHttpRequest request) {
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
	
	public static FullHttpResponse buildhttpresponse(HttpVersion version,HttpResponseStatus status,String message,Cookie cookie)
	{
			FullHttpResponse response = new DefaultFullHttpResponse(version,status);
			if(cookie!=null){
				response.headers().set("Set-Cookie",ClientCookieEncoder.STRICT.encode(cookie));
			}
			if(message==null)return response;
			ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
			response.content().writeBytes(buffer);
			return response;
		
	} 
	
	public static String checkssoredirect(FullHttpRequest request){
		String uri = request.uri();
		String[] strs = uri.split("/");
		LOGGER.info("uri 分解长度:"+strs.length);
		if(strs.length==3){
			String tid = strs[2];
			if(tokenidcache.contains(tid)){
				return tid;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

}
