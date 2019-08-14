package customer_service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import RegistryClient.ConfigFuture;
import configutils.NormalConfig;

@Configuration
public class ServiceConfig {
	@Autowired
	private ConfigFuture configFuture;
	
	@Bean("normalConfig")
	@DependsOn("serviceHolder")
	public NormalConfig normalConfig(){
		try {
			return configFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
