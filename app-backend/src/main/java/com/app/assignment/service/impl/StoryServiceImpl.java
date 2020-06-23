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

import com.app.assignment.exception.ThreadExecutionException;
import com.app.assignment.model.Story;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.service.StoryService;
import com.app.assignment.service.UserService;
import com.app.assignment.util.ListUtility;

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

	/**
	 * This method retrieves each story details by using story id returned by hacker
	 * new api.
	 * 
	 */
	@Override
	public List<Story> getTopStories() {
		logger.info("Enter: StoryServiceImpl.getTopStories");
		List<Story> list = new ArrayList<>();
		List<Integer> topIds = hackerNewsProxyService.getTopStories();
		CountDownLatch latch = new CountDownLatch(topIds.size());
		for (Integer i : topIds) {
			executor.submit(() -> {
				Story s = hackerNewsProxyService.getStory(String.valueOf(i));
				list.add(s);
				latch.countDown();
			});

		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("latch error",e);
			throw new ThreadExecutionException(e.getMessage());
		}
		logger.info("Exit: StoryServiceImpl.getTopStories");
		return list;
	}

	/**
	 * This method sort the story list in desc order by score.
	 * 
	 */
	@Override
	public List<Story> sortTopStories(List<Story> list) {
		logger.info("Enter: StoryServiceImpl.sortTopStories");
		List<Story> sortedList = ListUtility.sortStoryListByScoreDesc(list);
		logger.info("Exit: StoryServiceImpl.sortTopStories");
		return sortedList;
	}

}
