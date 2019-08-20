package RegistryParamConfigUtil;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("paramConfig")
@ConfigurationProperties(prefix="registry")
public class ParamConfig {
	private String serverip;
	private int serverport;
	public String getServerip() {
		return serverip;
	}
	public void setServerip(String serverip) {
//		System.out.println("set serverip:"+serverip);
		this.serverip = serverip;
	}
	public int getServerport() {
		return serverport;
	}
	public void setServerport(int serverport) {
		this.serverport = serverport;
	}
	
}
