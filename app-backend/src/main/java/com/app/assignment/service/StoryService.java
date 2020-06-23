package com.app.assignment.service;

import java.util.List;

import com.app.assignment.model.Story;

public interface StoryService {

	public List<Story> getTopStories();
	
	public List<Story> sortTopStories(List<Story> list);
	
}
