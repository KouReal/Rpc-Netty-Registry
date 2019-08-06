package RpcServer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import RegistryClient.RegistryClient;
import annotationutils.MyService;
import springutils.SpringContextUtil;

@Component
public class ServiceHolder{
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHolder.class);
	
	//private RegistryClient registryClient;
	
	@Value("${registryserver.ip}")
	private String RegServerIp;
	
	@Value("${registryserver.port}")
	private int RegServerPort;
	
	public static Logger getLogger() {
		return LOGGER;
	}




	public String getRegServerIp() {
		return RegServerIp;
	}




	public int getRegServerPort() {
		return RegServerPort;
	}




	@Autowired
	private SpringContextUtil springContextUtil;
	
	private Map<String, Object> serviceBeanMap = new HashMap<String, Object>();
	


	
	/**
     * 注册所有服务
     */
    
	public void registerAllService(String addr) {
    	
    	ApplicationContext applicationContext = springContextUtil.getApplicationContext();
    	Map<String, Object> beanmap = applicationContext.getBeansWithAnnotation(MyService.class);
    	
    	if(beanmap==null || beanmap.isEmpty()){
    		LOGGER.debug("需要注册的service为空");
    		return ;
    	}

        MyService myService;
        String serviceName;
        Object serviceBean;
        for (Map.Entry<String, Object> entry : beanmap.entrySet()) {
            //service实例
            serviceBean = entry.getValue();
            myService = serviceBean.getClass().getAnnotation(MyService.class);
            //service接口名称
            serviceName = myService.value();
            /*//注册
            this.serviceRegistry.register(serviceName, this.addr);
            serviceInstanceMap.put(serviceName, serviceBean);*/
            LOGGER.debug("register service: {} => {}", serviceName, addr);
        }
    }
	
}
