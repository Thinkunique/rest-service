package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.app.assignment.model.Story;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.service.StoryService;
import com.app.assignment.service.UserService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class StoryServiceImpl implements StoryService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	HackerNewsProxyService hackerNewsProxyService;

	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executor;

	@Autowired
	UserService userService;

	@Override
	public List<Story> getTopStories() throws InterruptedException {
		logger.info("Enter: StoryServiceImpl.getTopStories");
		Gson gson = JsonConverter.getGson();
		List<Story> list = new ArrayList<>();
		List<Integer> topIds = hackerNewsProxyService.getTopStories();
		System.out.print(topIds);
		CountDownLatch latch = new CountDownLatch(topIds.size());
		for (Integer i : topIds) {
			executor.submit(() -> {
				Story s = hackerNewsProxyService.getStory(String.valueOf(i));
				list.add(s);
				latch.countDown();
			});

		}
		latch.await();
		logger.info("Exit: StoryServiceImpl.getTopStories");
		return list;
	}

	@Override
	public List<Story> sortTopStories(List<Story> list) {
		logger.info("Enter: StoryServiceImpl.sortTopStories");
		List<Story> sortedList = list.stream().sorted(Comparator.comparingInt(Story::getScore).reversed())
				.collect(Collectors.toList());
		List<Story> topList = sortedList.subList(0, 10);
		logger.info("Exit: StoryServiceImpl.sortTopStories");
		return topList;
	}

}
