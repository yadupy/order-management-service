package com.accenture.pip.ordermanagementservice;

import com.netflix.discovery.EurekaNamespace;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;


@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@OpenAPIDefinition
@ComponentScan(basePackages = "com.accenture.pip")
@EnableDiscoveryClient
public class OrderManagementServiceApplication {



	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(OrderManagementServiceApplication.class);
		Environment environment = app.run(args).getEnvironment();
		getEnvDetails(environment);

	}

	private static void getEnvDetails(Environment env) {
/*
		String password = "123456";
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		log.info("encoded passowrd :{}",encodedPassword);*/

		String port = env.getProperty("server.port");
		String applicationName = env.getProperty("spring.application.name");
		log.info("\n------------------------------------------------------------\n\t"+
				"Application {} is running  on port : {}\n"+
				"\n------------------------------------------------------------",applicationName,port);

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
