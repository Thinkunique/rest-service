package com.app.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.assignment.model.Story;
import com.app.assignment.service.StoryService;

@RestController
public class AppController {

	@Autowired
	StoryService storyService;
	
	@GetMapping("/top-stories")
	public List<Story> getTopStories()
	{
		List<Story> list=storyService.getTopStories();
		return list;
	}
	
	@GetMapping("/comments")
	public List<Story> getComments()
	{
		List<Story> list=storyService.getTopStories();
		return list;
	}
	
	
	@GetMapping("/past-stories")
	public List<Story> getPastStories()
	{
		List<Story> list=storyService.getTopStories();
		return list;
	}
	
}
