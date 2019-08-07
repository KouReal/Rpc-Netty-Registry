package RpcServer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import RegistryClient.RegistryClient;
import annotationutils.MyService;
import configutils.ServiceRegist;
import springutils.SpringContextUtil;

@Component
public class ServiceHolder{
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHolder.class);
	
	@Value("${rpcserver.ip}")
	private String serverip;
	
	@Value("${rpcserver.port}")
	private int serverport;
	
	@Autowired
	private RegistryClient registryClient;

	@Autowired
	private SpringContextUtil springContextUtil;
	
	private Map<String, Object> beanmap = new HashMap<String, Object>();

	/**
     * 注册所有服务
     */
    
	@PostConstruct
	public void registAllService() {
    	
    	ApplicationContext applicationContext = springContextUtil.getApplicationContext();
    	beanmap = applicationContext.getBeansWithAnnotation(MyService.class);
    	
    	if(beanmap==null || beanmap.isEmpty()){
    		LOGGER.info("需要注册的service为空");
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
            String addr = serverip+String.valueOf(serverport);
            LOGGER.info("register service: {} => {}", serviceName, addr);
            Header header = new Header(1, Header.REGISTRY_SERVICEREGIST);
            ServiceRegist serviceRegist = new ServiceRegist(serviceName, addr);
            RegistryMessage registryMessage = new RegistryMessage(header, (Object)serviceRegist);
            LOGGER.info("构造注册信息:{}",registryMessage);
            try {
				registryClient.sendtocenter(registryMessage);
			} catch (Exception e) {
				LOGGER.info("registryclient 发送注册信息失败：{}",e.getMessage());
			}
        }
    }
	
}
