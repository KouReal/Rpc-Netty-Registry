package tradeinfo_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"tradeinfo_service","RpcServer","springutils","RegistryClient","TokenUtils","reflectionutils"})
public class apptradeinfo {
	public static void main(String[] args) {
		SpringApplication.run(apptradeinfo.class, args);
	}
}
