package testservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:conf.properties")
public class Configurations {
	private Logger log = LoggerFactory.getLogger(Configurations.class);
	@Autowired
	Shop shop;
	@Bean(value="Car")
	public Car Car(){
		log.debug("thread:{}",Thread.currentThread());
		System.out.println("in configurations");
		return shop.getCar();
	}
}
