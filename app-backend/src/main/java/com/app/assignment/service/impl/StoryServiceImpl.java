package com.app.assignment.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.Story;
import com.app.assignment.service.StoryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public List<Story> getTopStories() {
		GsonBuilder builder = new GsonBuilder(); 

		// Register an adapter to manage the date types as long values 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
		   public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		      return new Date(json.getAsJsonPrimitive().getAsLong()); 
		   } 
		});
		
		
		//GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
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

}
