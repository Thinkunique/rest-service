package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.Story;
import com.app.assignment.model.User;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.service.StoryService;
import com.app.assignment.service.UserService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	HackerNewsProxyService hackerNewsProxyService;
	
	@Autowired
    @Qualifier("cachedThreadPool")
    private ExecutorService executor;
	
	@Autowired
	UserService userService;
	
	@Override
	public List<Story> getTopStories() throws InterruptedException {
		
		long lStartTime = System.currentTimeMillis();
		
		Gson gson = JsonConverter.getGson();
		List<Story> list=new ArrayList<>();
		List<Integer> topIds=hackerNewsProxyService.getTopStories();

		
		System.out.print(topIds);
		CountDownLatch latch = new CountDownLatch(topIds.size());
		for(Integer i:topIds)
		{
			
			executor.submit(()->{
			
				Story s= hackerNewsProxyService.getStory(String.valueOf(i));
				
				list.add(s);
				//System.out.println(s);
				
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
	public List<Story> sortTopStories(List<Story> list) {
		List<Story> sortedList=list.stream().sorted(Comparator.comparingInt(Story::getScore).reversed()).collect(Collectors.toList());
		List<Story> topList=sortedList.subList(0,10);
		return topList;
	}

}
