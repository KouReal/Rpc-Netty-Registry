package testservice;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import RpcServer.ServiceHolder;
import springutils.SpringContextUtil;


@Component(value="TestDemo")
@PropertySource(value="classpath:conf.properties")
public class TestDemo {
	Logger log = LoggerFactory.getLogger(TestDemo.class);
	@Autowired
	private SpringContextUtil springContextUtil;
	
	@Autowired
	private ServiceHolder serviceHolder;
	
	
	
	/*@PostConstruct
	public void registservice(){
		log.debug("ServiceHolder屬性：{}",serviceHolder.getRegServerIp()+serviceHolder.getRegServerPort());
		log.debug("in TestDemo");
		HelloService hs = (HelloService) springContextUtil.getApplicationContext().getBean("HelloService");
		hs.sayhello();
		serviceHolder.registerAllService("myaddr");
	}*/
	
	

}
