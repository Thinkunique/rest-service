package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;
import com.app.assignment.service.CommentService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("cachedThreadPool")
    private ExecutorService executor;

	public Future<Map<String, List<Integer>>> calculateAsync(int id) throws InterruptedException {

		Map<String, List<Integer>> h = new ConcurrentHashMap<>();

		Gson gson = JsonConverter.getGson();
		// = new CompletableFuture<>();

		CompletableFuture<Map<String, List<Integer>>> completableFuture = CompletableFuture.supplyAsync(() -> {

			Map m = (Map) restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/" + id + ".json",
					Object.class);
			new Gson().toJson(m, Map.class);
			Item childComment = gson.fromJson(gson.toJson(m, Map.class), Item.class);

			if (childComment.getId() != null && childComment.getKids() != null) {
				h.put(childComment.getId(), childComment.getKids());
			} else if (childComment.getId() != null && childComment.getKids() == null) {
				h.put(childComment.getId(), new ArrayList<>());
			}

			return h;

		});

		return completableFuture;
	}

	@Override
	public List<Comment> getTopComments(int id) throws InterruptedException {

		long lStartTime = System.currentTimeMillis();
		
		Gson gson = JsonConverter.getGson();
		Map st = (Map) restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/" + id + ".json",
				Object.class);
		new Gson().toJson(st, Map.class);
		Item s = gson.fromJson(gson.toJson(st, Map.class), Item.class);
		System.out.println(s);
		
		Map<Integer,Integer> counts=new ConcurrentHashMap<>();
		

		//ExecutorService executor = Executors.newCachedThreadPool();
		
		CountDownLatch latch = new CountDownLatch(s.getKids().size());
		for (Integer i : s.getKids()) {

			executor.submit(()->{
				try {
					int count = 0;
					count+=loop(i);
					counts.put(i, count);					
					System.out.println("count>>>"+count);
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			
		}
		latch.await();
		//executor.shutdown();
		System.out.println("------------------------------------");
		for(Map.Entry<Integer, Integer> entry:counts.entrySet())
		{
			System.out.println(entry.getKey()+"------>"+entry.getValue());
		}
		
		System.out.println("count size: "+s.getKids().size());
		System.out.println("counts size: "+counts.size());
		
		long lEndTime = System.currentTimeMillis();

        long output = lEndTime - lStartTime;
        
        System.out.println("Elapsed time in milliseconds: " + output/1000);
		
		return null;
	}

	private int loop(int kid) throws InterruptedException {

		Future<Map<String, List<Integer>>> future = calculateAsync(kid);
		try {

			Map<String, List<Integer>> h = future.get();
			
			int c = 0;

			if (h.get(String.valueOf(kid)) != null && !h.get(String.valueOf(kid)).isEmpty()) {

				c += h.get(String.valueOf(kid)).size();

				System.out.println("c=="+c+" -->" + future.get());

				for (Integer i : h.get(String.valueOf(kid))) {
					c+=loop(i);
				}
				
				return c;

			} 
		} catch (InterruptedException | ExecutionException e) {

		}
		return 0;
	}

		
}
