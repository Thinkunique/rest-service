package com.app.assignment.service;

import java.util.List;

import com.app.assignment.model.Story;

public interface StoryService {

	public List<Story> getTopStories() throws InterruptedException;
	
	public List<Story> sortTopStories(List<Story> list);
	
}
