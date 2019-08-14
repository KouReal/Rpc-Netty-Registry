package customer_service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
@ConfigurationProperties(prefix="testdata.registry")
public class testyml {
	private static Logger logger = LoggerFactory.getLogger(testyml.class);
	private String serverip;
	
	private int serverport;

	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public int getServerport() {
		return serverport;
	}

	public void setServerport(int serverport) {
		this.serverport = serverport;
	}
	
	@PostConstruct
	public void init(){
		logger.info("serverip:{}",serverip);
	}

	
	
}
