package RegistryServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages={"RegistryServer","RegistryParamConfigUtil","configutils","springutils"})
public class AppRegistryServer {
	public static void main(String[] args) {
		SpringApplication.run(AppRegistryServer.class, args);
		
	}
}
