package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.app.assignment.model.Story;
import com.app.assignment.service.StoryService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public List<Story> getTopStories() {
		Gson gson = JsonConverter.getGson();
		List<Story> list=new ArrayList<>();
		List<Integer> topIds=(List<Integer>)restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/topstories.json",Object.class);
		List<Integer> topList=topIds.subList(0,10);
		System.out.print(topList);
		for(Integer i:topList)
		{
			Map st=(Map)restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/"+i+".json",Object.class);
			new Gson().toJson(st, Map.class);
			Story s=gson.fromJson(gson.toJson(st, Map.class), Story.class);
			list.add(s);
			System.out.println(s);
		}
		return list;
	}

	@Override
	public List<Story> getPastStories() {
		// TODO Auto-generated method stub
		return null;
	}

}
