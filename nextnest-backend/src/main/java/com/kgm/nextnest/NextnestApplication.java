package com.kgm.nextnest;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
		info=@Info(
				title = "SpringBoot NextNest REST APIs",
				description = "SpringBoot NextNest REST APIs documentation",
				version="v1.0",
				contact = @Contact(
						name = "Kazi Golam Mahtab",
						email = "kazimahtab@gmail.com",
						url = "https://www.kgmgroup.net"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.kgmgroup.net/license"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "SpringBoot NextNest Documentation",
				url = "https://github.com/Mahtab0204/springboot-nextnest-rest-api"
		)
)
public class NextnestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextnestApplication.class, args);
	}
}