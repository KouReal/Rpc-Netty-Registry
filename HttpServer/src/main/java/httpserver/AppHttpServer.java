package httpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"httpserver","MessageUtils"})
public class AppHttpServer {

	public static void main(String[] args) {
		SpringApplication.run(AppHttpServer.class, args);
	}

}