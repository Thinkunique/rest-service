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
import com.app.assignment.util.ListUtility;

@Service
public class AppServiceImpl implements AppService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	StoryService storyService;

	@Autowired
	CommentService commentService;

	/**
	 *  This service is used to fetch the top 10 parent comments
	 *  on given story.
	 *  
	 * */
	@Override
	@Cacheable(value = "ten-minute-cache", key = "'CommentCache'+#storyId")
	public List<Comment> getComments(int storyId) {
		logger.info("Enter: AppServiceImpl.getComments-[{}]",storyId);
		List<Comment> list = null;
		list = commentService.getTopComments(storyId);
		logger.info("Exit: AppServiceImpl.getComments-[{}]",storyId);
		return list;
	}

	
	/**
	 *  This service is used to fetch the top 10 stories
	 *  based on score.
	 *  
	 * */
	@Override
	@Cacheable(value = "ten-minute-cache", key = "'TopStory'")
	public List<Story> getTopStories() {
		logger.info("Enter: AppServiceImpl.getTopStories");
		List<Story> resultList = null;
		List<Story> list = storyService.getTopStories();
		List<Story> sortList = storyService.sortTopStories(list);
		resultList = ListUtility.subListByRange(sortList, 0, 10);
		logger.info("Exit: AppServiceImpl.getTopStories");
		return resultList;
	}

	/**
	 *  
	 *  This service is used to fetch the top past stories.
	 *
	 * */
	@Override
	@Cacheable(value = "ten-minute-cache", key = "'PastStory'")
	public List<Story> getPastStories() {
		logger.info("Enter: AppServiceImpl.getPastStories");
		List<Story> list = storyService.getTopStories();
		logger.info("Exit: AppServiceImpl.getPastStories");
		return list;
	}

}
