package com.app.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.app.assignment.controller","com.app.assignment.service.impl"})
public class AppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppBackendApplication.class, args);
	}

}
