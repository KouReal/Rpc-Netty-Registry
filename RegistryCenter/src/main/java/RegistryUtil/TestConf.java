package RegistryUtil;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
@ConfigurationProperties(prefix="testdata")
public class TestConf {
	private List<String> animals;

	public List<String> getAnimals() {
		return animals;
	}

	public void setAnimals(List<String> animals) {
		this.animals = animals;
	}
	
}
