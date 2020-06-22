package com.app.assignment.proxy.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
		try {
			result = (List<Integer>) restTemplate.getForObject(topStoriesURL, Object.class);
		} catch (Exception e) {
			logger.error("Error while getting list of top stories from hacker news api", e);
		}
		logger.info("Exit HackerNewsProxyServiceImpl.getTopStories");
		return result;
	}

	public User getUserDetails(String id) {
		logger.info("Enter: HackerNewsProxyServiceImpl.getUserDetails-[{}]",id);
		User user = null;
		try {
			String url = userURL + id + AppConstants.JSON;
			Map m = (Map) restTemplate.getForObject(url, Object.class);
			user = gson.fromJson(gson.toJson(m, Map.class), User.class);
		} catch (Exception e) {
			logger.error("Error while fetching user details from hacker news api", e);
		}
		logger.info("Exit: HackerNewsProxyServiceImpl.getUserDetails-[{}]",id);
		return user;
	}

	@Override
	public Item getItem(String id) {
		logger.info("Enter: HackerNewsProxyServiceImpl.getItem-[{}]",id);
		Item item = null;
		try {
			String url = itemURL + id + AppConstants.JSON;
			Map m = (Map) restTemplate.getForObject(url, Object.class);
			item = gson.fromJson(gson.toJson(m, Map.class), Item.class);
		} catch (Exception e) {
			logger.error("Error while fetching item from hacker news api", e);
		}
		logger.info("Exit: HackerNewsProxyServiceImpl.getItem-[{}]",id);
		return item;
	}

	@Override
	public Story getStory(String id) {
		logger.info("Enter: HackerNewsProxyServiceImpl.getStory-[{}]",id);
		Story story = null;
		try {
			String url = itemURL + id + AppConstants.JSON;
			Map m = (Map) restTemplate.getForObject(url, Object.class);
			story = gson.fromJson(gson.toJson(m, Map.class), Story.class);
		} catch (Exception e) {
			logger.error("Error while fetching story from hacker news api", e);
		}
		logger.info("Exit: HackerNewsProxyServiceImpl.getStory-[{}]",id);
		return story;
	}

}
