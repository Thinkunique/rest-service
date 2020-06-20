package com.app.assignment.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;
import com.app.assignment.model.Story;
import com.app.assignment.service.CommentService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public List<Comment> getComments(int storyId) {
		
		Gson gson = JsonConverter.getGson();
		Map st=(Map)restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/"+storyId+".json",Object.class);
		new Gson().toJson(st, Map.class);
		Story s=gson.fromJson(gson.toJson(st, Map.class), Story.class);
		System.out.println(s);
		List<Integer> list=s.getKids();
		for(Integer i:list)
		{
			Map m=(Map)restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/"+i+".json",Object.class);
			new Gson().toJson(m, Map.class);
			Item item=gson.fromJson(gson.toJson(m, Map.class), Item.class);
			System.out.println(item);
		}
		return null;
	}

}
