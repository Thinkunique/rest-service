package com.app.assignment.service;

import java.util.List;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Story;

public interface AppService {

	public List<Comment> getComments(int storyId);
	
	public List<Story> getTopStories();
	
	public List<Story> getPastStories();
}
