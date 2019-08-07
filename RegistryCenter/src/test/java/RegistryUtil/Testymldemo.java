package RegistryUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class Testymldemo {
	Logger log = LoggerFactory.getLogger(Testymldemo.class);

    @Autowired
    private Uniformconfig ufc;
    
    @Autowired
    private Ssoconfig scf;
    
    @Autowired
    private TestConf tf;
    
    @Autowired
    private Serviceconfig serviceconfig;



    @Test
    public void testuniformconfig(){
    	//this.resource
    	//System.out.println(y0.getUniformconfig().getClientConnectTimeout());
    	System.out.println(ufc.toString());
    	System.out.println(scf.getSsoconfig());
    	System.out.println(tf.getAnimals());
    	System.out.println(serviceconfig.toString());
    }
}
