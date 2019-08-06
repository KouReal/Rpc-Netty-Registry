package testservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import annotationutils.MyService;

@Component(value="HelloService")
@MyService(value="HelloService")
public class HelloService {
	Logger log = LoggerFactory.getLogger(HelloService.class);
	public String sayhello(){
		log.info("in HelloService: sayhello");
		return "hello world";
	}
}
