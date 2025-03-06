package com.criclab.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CricLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricLabApplication.class, args);
	}

}
