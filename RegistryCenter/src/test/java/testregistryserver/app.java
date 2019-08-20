package testregistryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springutils.SpringContextStatic;

@SpringBootApplication
@ComponentScan(basePackages={"testregistryserver","RegistryServer","RegistryParamConfigUtil","MessageUtils","springutils"})
public class app{
	
	public static void main(String[] args) {
		SpringApplication.run(app.class, args);
		
	}
}
