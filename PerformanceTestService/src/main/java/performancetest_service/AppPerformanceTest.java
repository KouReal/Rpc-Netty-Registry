package performancetest_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"performancetest_service","RpcServer","springutils","configutils","TokenUtils","reflectionutils"})
public class AppPerformanceTest {
	public static void main(String[] args) {
		SpringApplication.run(AppPerformanceTest.class, args);
	}
}
