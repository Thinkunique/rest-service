package com.app.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Story;
import com.app.assignment.service.AppService;

@RestController
public class AppController {

	@Autowired
	AppService appService;
	
	@GetMapping("/top-stories")
	public List<Story> getTopStories()
	{
		List<Story> list=appService.getTopStories();
		return list;
	}
	
	@GetMapping("/{storyId}/comments")
	public List<Comment> getComments(@PathVariable("storyId") int storyId)
	{
		List<Comment> list=appService.getComments(storyId);
		return list;
	}
	
	
	@GetMapping("/past-stories")
	public List<Story> getPastStories()
	{
		List<Story> list=appService.getPastStories();
		return list;
	}
	
}
