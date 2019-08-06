package testservice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import testservice.Car;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpringboot {
	Logger log = LoggerFactory.getLogger(TestSpringboot.class);

    @Autowired
    private Car car;
    
    @Autowired
    private Shop shop;

    @Test
    public void getLearn(){
    	log.debug("thread:{}",Thread.currentThread());
        log.debug("car name:{}",car.name);
        log.debug("shop name:{}",shop.shopname);
    }
}