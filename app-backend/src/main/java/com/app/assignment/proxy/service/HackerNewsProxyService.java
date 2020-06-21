package com.app.assignment.proxy.service;

import java.util.List;

import com.app.assignment.model.Item;
import com.app.assignment.model.Story;
import com.app.assignment.model.User;

public interface HackerNewsProxyService {

	public List<Integer> getTopStories();
	
	public User getUserDetails(String id);
	
	public Item getItem(String id);
	
	public Story getStory(String id);
}
