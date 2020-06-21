package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	@Autowired
    @Qualifier("cachedThreadPool")
    private ExecutorService executor;
	
	@Override
	public List<Story> getTopStories() throws InterruptedException {
		
		long lStartTime = System.currentTimeMillis();
		
		Gson gson = JsonConverter.getGson();
		List<Story> list=new ArrayList<>();
		List<Integer> topIds=(List<Integer>)restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/topstories.json",Object.class);
		List<Integer> topList=topIds.subList(0,10);
		System.out.print(topList);
		CountDownLatch latch = new CountDownLatch(10);
		for(Integer i:topList)
		{
			
			executor.submit(()->{
			
				Map st=(Map)restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/"+i+".json",Object.class);
				new Gson().toJson(st, Map.class);
				Story s=gson.fromJson(gson.toJson(st, Map.class), Story.class);
				list.add(s);
				System.out.println(s);
				
				latch.countDown();
			});
			
		}
		
		latch.await();
		long lEndTime = System.currentTimeMillis();

        long output = lEndTime - lStartTime;
        
        System.out.println("Elapsed time in milliseconds: " + output/1000);
		
		return list;
	}

	@Override
	public List<Story> getPastStories() {
		// TODO Auto-generated method stub
		return null;
	}

}
