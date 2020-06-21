package com.app.assignment.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.User;
import com.app.assignment.service.UserService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	@Qualifier("cachedThreadPool")
	ExecutorService executor;

	@Override
	public User getUserDetails(String id) {

		Gson gson = JsonConverter.getGson();

		CompletableFuture<User> completableFuture = CompletableFuture.supplyAsync(() -> {

			Map m = (Map) restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/user/" + id + ".json",
					Object.class);
			new Gson().toJson(m, Map.class);
			User user = gson.fromJson(gson.toJson(m, Map.class), User.class);

			LocalDate today = LocalDate.now();
			LocalDate birthday = Instant.ofEpochMilli(user.getCreated().getTime()).atZone(ZoneId.systemDefault())
					.toLocalDate();

			Period p = Period.between(birthday, today);

			user.setAge(p.getYears());

			return user;

		}, executor);

		User user = null;
		try {
			user = completableFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return user;

	}

}
