package tradeinfo_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"account_service","RpcServer","springutils","RegistryClient","TokenUtils","reflectionutils"})
public class appaccount {
	public static void main(String[] args) {
		SpringApplication.run(appaccount.class, args);
	}
}
