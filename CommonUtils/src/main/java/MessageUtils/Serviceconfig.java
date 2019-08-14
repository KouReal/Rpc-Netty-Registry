package MessageUtils;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix="rpcconfig.dataconfig")
public class Serviceconfig implements Serializable{
	private List<serviceaddr> serviceconfig;

	public List<serviceaddr> getServiceconfig() {
		return serviceconfig;
	}

	public void setServiceconfig(List<serviceaddr> serviceconfig) {
		this.serviceconfig = serviceconfig;
	}

	@Override
	public String toString() {
		return "Serviceconfig [serviceconfig=" + serviceconfig + "]";
	}
	
}
