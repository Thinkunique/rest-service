package com.app.assignment.service.impl;

import java.util.List;

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

	@Autowired
	StoryService storyService;
	
	@Autowired
	CommentService commentService;
	
	@Override
	@Cacheable(value="ten-second-cache",key="'CommentCache'+#id")
	public List<Comment> getComments(int storyId) {
		try {
			return commentService.getTopComments(storyId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Cacheable(value="ten-second-cache",key="'Story'")
	public List<Story> getTopStories() {
		try {
			return storyService.getTopStories();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Story> getPastStories() {
		return storyService.getPastStories();
	}

}
