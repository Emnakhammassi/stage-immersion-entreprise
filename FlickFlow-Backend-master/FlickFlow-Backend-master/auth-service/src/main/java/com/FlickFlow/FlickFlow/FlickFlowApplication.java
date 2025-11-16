package com.FlickFlow.FlickFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class FlickFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlickFlowApplication.class, args);
	}

}
