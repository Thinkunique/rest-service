package com.app.assignment.proxy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.Item;
import com.app.assignment.model.Story;
import com.app.assignment.model.User;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.util.AppConstants;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class HackerNewsProxyServiceImpl implements HackerNewsProxyService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${hackernews.user.url}")
	private String userURL;

	@Value("${hackernews.item.url}")
	private String itemURL;

	@Value("${hackernews.top.stories.url}")
	private String topStoriesURL;

	public List<Integer> getTopStories() {
		List<Integer> result = (List<Integer>) restTemplate.getForObject(topStoriesURL, Object.class);
		return result;
	}

	public User getUserDetails(String id) {
		Gson gson = JsonConverter.getGson();

		String url = userURL + id + AppConstants.JSON;

		Map m = (Map) restTemplate.getForObject(url, Object.class);

		User user = gson.fromJson(gson.toJson(m, Map.class), User.class);

		return user;
	}

	@Override
	public Item getItem(String id) {

		Gson gson = JsonConverter.getGson();

		String url = itemURL + id + AppConstants.JSON;

		Map m = (Map) restTemplate.getForObject(url, Object.class);

		Item item = gson.fromJson(gson.toJson(m, Map.class), Item.class);

		return item;
	}

	@Override
	public Story getStory(String id) {

		Gson gson = JsonConverter.getGson();

		String url = itemURL + id + AppConstants.JSON;

		Map m = (Map) restTemplate.getForObject(url, Object.class);

		Story story = gson.fromJson(gson.toJson(m, Map.class), Story.class);

		return story;

	}

}
