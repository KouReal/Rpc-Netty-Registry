package customer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"customer_service","RpcServer","springutils","RegistryClient","TokenUtils","reflectionutils"})
public class appcustomer {
	public static void main(String[] args) {
		SpringApplication.run(appcustomer.class, args);
	}
}
