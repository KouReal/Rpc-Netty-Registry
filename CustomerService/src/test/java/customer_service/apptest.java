package customer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"customer_service","springutils","reflectionutils"})
public class apptest {

	public static void main(String[] args) {
		SpringApplication.run(apptest.class, args);

	}

}
