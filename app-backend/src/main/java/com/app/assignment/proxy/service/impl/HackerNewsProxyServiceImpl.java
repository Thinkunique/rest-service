package com.app.assignment.proxy.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.exception.ItemNotFoundException;
import com.app.assignment.exception.StoryNotFoundException;
import com.app.assignment.exception.UserNotFoundException;
import com.app.assignment.model.Item;
import com.app.assignment.model.Story;
import com.app.assignment.model.User;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.util.AppConstants;
import com.google.gson.Gson;

@Service
public class HackerNewsProxyServiceImpl implements HackerNewsProxyService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	RestTemplate restTemplate;

	@Value("${hackernews.user.url}")
	private String userURL;

	@Value("${hackernews.item.url}")
	private String itemURL;

	@Value("${hackernews.top.stories.url}")
	private String topStoriesURL;

	@Autowired
	private Gson gson;

	public List<Integer> getTopStories() {
		logger.info("Enter HackerNewsProxyServiceImpl.getTopStories");
		List<Integer> result = null;
		result = (List<Integer>) restTemplate.getForObject(topStoriesURL, Object.class);
		if (result == null) {
			logger.error("top stories not found from hacker news api");
			throw new StoryNotFoundException("Stories Not Found");
		}
		logger.info("Exit HackerNewsProxyServiceImpl.getTopStories");
		return result;
	}

	public User getUserDetails(String id) {
		logger.info("Enter: HackerNewsProxyServiceImpl.getUserDetails-[{}]", id);
		User user = null;
		String url = userURL + id + AppConstants.JSON;
		Map response = (Map) restTemplate.getForObject(url, Object.class);
		if (response == null) {
			logger.error("user details not found from hacker news api");
			throw new UserNotFoundException("User not found: " + id);
		} else {
			user = gson.fromJson(gson.toJson(response, Map.class), User.class);
		}
		logger.info("Exit: HackerNewsProxyServiceImpl.getUserDetails-[{}]", id);
		return user;
	}

	@Override
	public Item getItem(String id) {
		logger.info("Enter: HackerNewsProxyServiceImpl.getItem-[{}]", id);
		Item item = null;
		String url = itemURL + id + AppConstants.JSON;
		Map response = (Map) restTemplate.getForObject(url, Object.class);
		if (response == null) {
			logger.error("Item not found from hacker news api");
			throw new ItemNotFoundException("Item not found: " + id);
		} else {
			item = gson.fromJson(gson.toJson(response, Map.class), Item.class);
		}
		logger.info("Exit: HackerNewsProxyServiceImpl.getItem-[{}]", id);
		return item;
	}

	@Override
	public Story getStory(String id) {
		logger.info("Enter: HackerNewsProxyServiceImpl.getStory-[{}]", id);
		Story story = null;
		String url = itemURL + id + AppConstants.JSON;
		Map response = (Map) restTemplate.getForObject(url, Object.class);
		if (response == null) {
			logger.error("Story not found from hacker news api");
			throw new StoryNotFoundException("Story not found: " + id);
		} else {
			story = gson.fromJson(gson.toJson(response, Map.class), Story.class);
		}
		logger.info("Exit: HackerNewsProxyServiceImpl.getStory-[{}]", id);
		return story;
	}

}
