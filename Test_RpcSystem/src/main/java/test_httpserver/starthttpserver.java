package test_httpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"httpserver","MessageUtils","RegistryClient","RpcClient","springutils"})

public class starthttpserver {

	public static void main(String[] args) {
		SpringApplication.run(starthttpserver.class, args);
	}

}