package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.assignment.model.Story;
import com.app.assignment.service.StoryService;

@Service
public class StoryServiceImpl implements StoryService {

	@Override
	public List<Story> getTopStories() {
		List<Story> list=new ArrayList<>();
		return list;
	}

}
