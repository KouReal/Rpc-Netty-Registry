package httpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import springutils.SpringContextStatic;

@SpringBootApplication
@ComponentScan(basePackages={"httpserver","RegistryClient","RpcClient","springutils"})

public class AppHttpServer {

	public static void main(String[] args) {
		ConfigurableApplicationContext runctx = SpringApplication.run(AppHttpServer.class, args);
		SpringContextStatic.runctx = runctx;
	}

}