package com.app.assignment.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Story;
import com.app.assignment.service.AppService;
import com.app.assignment.service.CommentService;
import com.app.assignment.service.StoryService;

@Service
public class AppServiceImpl implements AppService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	StoryService storyService;

	@Autowired
	CommentService commentService;

	@Override
	@Cacheable(value = "ten-minute-cache", key = "'CommentCache'+#id")
	public List<Comment> getComments(int storyId) {
		logger.info("Enter AppServiceImpl.getComments-[{}]",storyId);
		List<Comment> list = null;
		try {
			list = commentService.getTopComments(storyId);
		} catch (InterruptedException e) {
			logger.error("Error while retrieving comment-[{}]",storyId,e);
		}
		logger.info("Enter AppServiceImpl.getComments-[{}]",storyId);
		return list;
	}

	@Override
	@Cacheable(value = "ten-minute-cache", key = "'TopStory'")
	public List<Story> getTopStories() {
		logger.info("Enter AppServiceImpl.getTopStories");
		List<Story> sortList = null;
		try {
			List<Story> list = storyService.getTopStories();
			sortList = storyService.sortTopStories(list);
		} catch (InterruptedException e) {
			logger.error("Error while retrieving top stories", e);
		}
		logger.info("Enter AppServiceImpl.getTopStories");
		return sortList;
	}

	@Override
	@Cacheable(value = "ten-minute-cache", key = "'PastStory'")
	public List<Story> getPastStories() {
		logger.info("Enter AppServiceImpl.getPastStories");
		List<Story> list = null;
		try {
			list = storyService.getTopStories();
		} catch (InterruptedException e) {
			logger.error("Error while retrieving past stories", e);
		}
		logger.info("Enter AppServiceImpl.getPastStories");
		return list;
	}

}
