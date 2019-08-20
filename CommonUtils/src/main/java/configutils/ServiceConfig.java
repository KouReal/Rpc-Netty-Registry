package configutils;
import java.io.Serializable;
import java.util.List;

import org.omg.PortableServer.Servant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix="rpcconfig.dataconfig")
public class ServiceConfig implements Serializable{
	private List<ServiceAddr> serviceaddrs;

	public List<ServiceAddr> getServiceaddrs() {
		return serviceaddrs;
	}
	public void setServiceaddrs(List<ServiceAddr> serviceaddrs) {
		this.serviceaddrs = serviceaddrs;
	}

	@Override
	public String toString() {
		return "Serviceconfig [serviceaddrs=" + serviceaddrs + "]";
	}
	
}