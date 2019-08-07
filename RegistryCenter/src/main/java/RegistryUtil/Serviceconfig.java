package RegistryUtil;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix="rpcconfig.dataconfig")
public class Serviceconfig {
	private List<serviceport> serviceconfig;

	public List<serviceport> getServiceconfig() {
		return serviceconfig;
	}

	public void setServiceconfig(List<serviceport> serviceconfig) {
		this.serviceconfig = serviceconfig;
	}

	@Override
	public String toString() {
		return "Serviceconfig [serviceconfig=" + serviceconfig + "]";
	}
	
}
