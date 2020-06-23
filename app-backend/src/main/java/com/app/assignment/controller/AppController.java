package com.app.assignment.controller;

import java.util.List;

import javax.ws.rs.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Story;
import com.app.assignment.response.CommentResponse;
import com.app.assignment.response.StoryResponse;
import com.app.assignment.service.AppService;
import com.app.assignment.util.AppConstants;

import io.swagger.annotations.ApiOperation;

@RestController
public class AppController {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	AppService appService;

	@GetMapping(AppConstants.TOP_STORIES)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Top 10 stories from Hacker News", response = StoryResponse.class)
	public ResponseEntity<StoryResponse> getTopStories() {
		logger.info("Enter: AppController.topStories");
		StoryResponse response = new StoryResponse("Successfully retrieved top stories");
		List<Story> list = appService.getTopStories();
		response.setTotal(list.size());
		response.setStories(list);
		logger.info("Exit: AppController.topStories");
		return new ResponseEntity<StoryResponse>(response, HttpStatus.OK);
	}

	@GetMapping(AppConstants.TOP_STORY_COMMENTS)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Top 10 comments for given story", response = CommentResponse.class)
	public ResponseEntity<CommentResponse> getComments(@PathVariable("storyId") int storyId) {
		logger.info("Enter: AppController.getComments-[{}]", storyId);
		CommentResponse response = new CommentResponse("Successfully retrieved comments");
		List<Comment> list = appService.getComments(storyId);
		response.setTotal(list.size());
		response.setComments(list);
		logger.info("Exit: AppController.getComments-[{}]", storyId);
		return new ResponseEntity<CommentResponse>(response, HttpStatus.OK);
	}

	@GetMapping(AppConstants.PAST_TOP_STORIES)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Top past stories from Hacker News", response = StoryResponse.class)
	public ResponseEntity<StoryResponse> getPastStories() {
		logger.info("Enter: AppController.getPastStories");
		StoryResponse response = new StoryResponse("Successfully retrieved past stories");
		List<Story> list = appService.getPastStories();
		response.setStories(list);
		response.setTotal(list.size());
		logger.info("Exit: AppController.getPastStories");
		return new ResponseEntity<StoryResponse>(response, HttpStatus.OK);
	}

}
