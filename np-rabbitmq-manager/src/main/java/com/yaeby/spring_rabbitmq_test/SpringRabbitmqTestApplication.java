package com.yaeby.spring_rabbitmq_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringRabbitmqTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRabbitmqTestApplication.class, args);
	}

}
