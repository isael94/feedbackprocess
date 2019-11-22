package com.fs.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class FeedbackprocessApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbackprocessApplication.class, args);
	}

}
