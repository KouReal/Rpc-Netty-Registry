package testservice;

import javax.annotation.PostConstruct;

import org.apache.log4j.lf5.viewer.LogFactor5Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class Shop {
	private Logger log = LoggerFactory.getLogger(Shop.class);
	@Value("${shop.name}")
	public String shopname;
	
	private Car benzcar;
	@PostConstruct
	public void buildcar(){
		log.debug("thread:{}",Thread.currentThread());
		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String carname = shopname+"-benz";
		benzcar = new Car(carname);
	}
	public Car getCar(){
		return benzcar;
	}
}
