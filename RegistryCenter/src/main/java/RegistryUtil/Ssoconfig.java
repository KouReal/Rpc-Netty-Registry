package RegistryUtil;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="rpcconfig")
public class Ssoconfig {
	private List<ssogroup> ssoconfig;

	
	public List<ssogroup> getSsoconfig() {
		return ssoconfig;
	}


	public void setSsoconfig(List<ssogroup> ssoconfig) {
		this.ssoconfig = ssoconfig;
	}


	@Override
	public String toString() {
		return "ssoconfig [ssolist=" + ssoconfig + "]";
	}
	
}
