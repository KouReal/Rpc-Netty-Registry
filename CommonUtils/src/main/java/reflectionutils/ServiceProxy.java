package reflectionutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import TokenUtils.TokenCache;
import annotationutils.AuthToken;
import annotationutils.MyService;
import exceptionutils.RpcErrorException;
import springutils.SpringContextUtil;



@Component("serviceProxy")
public class ServiceProxy implements InvocationHandler{
	private static Logger logger = LoggerFactory.getLogger(ServiceProxy.class);
	private Map<String, Object> beanmap = new HashMap<String, Object>();
	private Map<String, Object> proxymap = new HashMap<String, Object>();
	private ThreadLocal<Object> target = new ThreadLocal<>();

	@Autowired
	private TokenCache tokenCache;
	@Autowired
	private SpringContextUtil sctx;

	@PostConstruct
	public void init(){
		
		 Map<String, Object> objmap = sctx.getApplicationContext().getBeansWithAnnotation(MyService.class);
		Object serviceBean = null;
		MyService myService;
		String serviceName;
		 for (Map.Entry<String, Object> entry : objmap.entrySet()) {
	            //service实例
	            serviceBean = entry.getValue();
	            myService = serviceBean.getClass().getAnnotation(MyService.class);
	            //service接口名称
	            serviceName = myService.value();
	            logger.info("bean map add:{}->{}",serviceName,serviceBean);
	            beanmap.put(serviceName, serviceBean);
		 }
	}
	
	public Object callservice(String servicename,String methodname,Object params) throws RpcErrorException{
		Object proxy = proxymap.get(servicename);
		Object bean = beanmap.get(servicename);
		//logger.info("before proxy:{}, bean:{}",proxy,bean);
		//SLF4J: Failed toString() invocation on an object of type [com.sun.proxy.$Proxy54]
		//logger.info("proxy isnot null:{},Thread:{}",proxy,Thread.currentThread());
		if(proxy==null){
			bean = beanmap.get(servicename);
			if(bean==null){
				throw new RpcErrorException("服务名："+servicename+"不存在");
			}
			Object newproxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), this);
			proxymap.put(servicename, newproxy);
			proxy = newproxy;
		}
		//logger.info("after proxy:{}",proxy.toString());
		target.set(bean);
			
//			Class<? extends Object> cls = proxy.getClass();
			Method method;
			Object result = null;
			try {
				method = proxy.getClass().getMethod(methodname,params.getClass());
				result = method.invoke(proxy, params);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		
	}

	@Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
		AuthToken annotation = method.getAnnotation(AuthToken.class);
		//logger.info("invoke get annotation={}",annotation.toString());
		if(annotation!=null){
			logger.info("annotation isnot null:{},Thread:{}",annotation,Thread.currentThread());
			String tokenid = (String)(((JSONObject)args[0]).get("tokenid"));
			logger.info("tokenid = {}",tokenid);
			boolean state = tokenCache.authtoken(tokenid);
			if(!state){
				JSONObject res = new JSONObject();
				res.put("result", "验证token身份失败");
				return res.toJSONString();
			}
		}
		logger.info("annotation is null,Thread:{}",Thread.currentThread());
		return method.invoke(target.get(), args);
	}
	
	
    
	
}
