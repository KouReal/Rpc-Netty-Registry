package RpcClient;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import MessageUtils.Uniformconfig;
import RegistryClient.ConfigFuture;
import RegistryClient.RegistryClient;
import configutils.NormalConfig;
import configutils.ServiceRegist;

@Configuration
public class ClientConfiguration {
	@Autowired
	private RegistryClient registryClient;
	@Autowired
	private ConfigFuture configFuture;
	
	@Bean("uniformConfig")
	@DependsOn(value={"registryClient","configFuture"})
	public Uniformconfig uniformConfig(){
		ServiceRegist serviceRegist = new ServiceRegist("httpserver", "127.0.0.1:xx");
		RegistryMessage msg = new RegistryMessage(new Header(1, Header.REGISTRY_SERVICE), serviceRegist);
		try {
			registryClient.sendtocenter(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NormalConfig normalConfig;
		try {
			normalConfig = configFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return normalConfig.getUniformconfig();
	}
}
