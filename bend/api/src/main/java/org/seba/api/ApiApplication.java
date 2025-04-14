package org.seba.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"org.seba.api",
		"org.seba.services",
		"org.seba.repositories",
		"org.seba.utils.jwt",
		"org.seba.configs"
})
@EnableJpaRepositories(basePackages = "org.seba.repositories")
@EntityScan(basePackages = "org.seba.entities")
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}

