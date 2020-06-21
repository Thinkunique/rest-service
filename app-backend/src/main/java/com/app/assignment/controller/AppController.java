package com.app.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Story;
import com.app.assignment.response.CommentResponse;
import com.app.assignment.response.StoryResponse;
import com.app.assignment.service.AppService;

@RestController
public class AppController {

	@Autowired
	AppService appService;
	
	@GetMapping("/top-stories")
	public StoryResponse getTopStories()
	{
		StoryResponse response=new StoryResponse();
		List<Story> list=appService.getTopStories();
		response.setTotal(list.size());
		response.setStories(list);
		return response;
	}
	
	@GetMapping("/{storyId}/comments")
	public CommentResponse getComments(@PathVariable("storyId") int storyId)
	{
		CommentResponse response=new CommentResponse();
		List<Comment> list=appService.getComments(storyId);
		response.setTotal(list.size());
		response.setComments(list);
		return response;
	}
	
	
	@GetMapping("/past-stories")
	public StoryResponse getPastStories()
	{
		StoryResponse response=new StoryResponse();
		List<Story> list=appService.getPastStories();
		response.setStories(list);
		response.setTotal(list.size());
		return response;
	}
	
}
