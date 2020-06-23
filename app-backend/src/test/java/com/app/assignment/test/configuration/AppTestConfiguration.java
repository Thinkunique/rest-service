package com.app.assignment.test.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.service.UserService;

@Profile("test")
@Configuration
@ComponentScan(basePackages={"com.app.assignment.controller","com.app.assignment.repo.impl","com.app.assignment.proxy.service.impl","com.app.assignment.configuration","com.app.assignment.service.impl","com.app.assignment.exception.handler"})
public class AppTestConfiguration {
	
	@Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
	
	@Bean
	public RestTemplate restTemplate() {
		return Mockito.mock(RestTemplate.class);
	}
	
	@Bean
	public HackerNewsProxyService hackerNewsProxyService() {
		return Mockito.mock(HackerNewsProxyService.class);
	}

}
