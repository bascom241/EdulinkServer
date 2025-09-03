package com.Edulink.EdulinkServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EdulinkServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdulinkServerApplication.class, args);
		System.out.println("Welcome to Edu link Server Application");
	}

}
