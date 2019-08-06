package testservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages={"testservice","RpcServer","springutils"})
public class app {
	static Logger log = LoggerFactory.getLogger(app.class);
	/*@Autowired
	static TestDemo testDemo;*/
	public static void main(String[] args) throws InterruptedException {
		SpringApplication application = new SpringApplication(app.class);
		log.debug("thread:{}",Thread.currentThread());
		ConfigurableApplicationContext act = application.run(args);
		
		
		
	}
}