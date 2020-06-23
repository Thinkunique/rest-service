package com.app.assignment.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.app.assignment.model.User;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.service.UserService;
import com.app.assignment.util.DateUtility;

@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	HackerNewsProxyService hackerNewsProxyService;

	@Autowired
	@Qualifier("cachedThreadPool")
	ExecutorService executor;

	/**
	 *  This method retrieves user details from hacker news api
	 *  and returns user with calculated age.
	 */
	@Override
	public User getUserDetails(String id) {
		logger.info("Enter: UserServiceImpl.getUserDetails-[{}]",id);
		CompletableFuture<User> completableFuture = CompletableFuture.supplyAsync(() -> {
			User user = hackerNewsProxyService.getUserDetails(id);
			user.setAge(DateUtility.getAge(user.getCreated().getTime()));
			return user;
		}, executor);
		User user = null;
		try {
			user = completableFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Error while fetching user details-[{}]",id, e);
		}
		logger.info("Exit: UserServiceImpl.getUserDetails-[{}]",id);
		return user;
	}

}
