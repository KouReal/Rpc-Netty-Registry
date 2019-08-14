package testregistryserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import RegistryServer.RegistryServer;
import configutils.NormalConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class testserver {
	@Autowired
	RegistryServer registryServer;
	@Autowired
	NormalConfig normalConfig;
	
	@Test
	public void test1(){
		System.out.println("in test");
		System.out.println(normalConfig);
	}
	
}