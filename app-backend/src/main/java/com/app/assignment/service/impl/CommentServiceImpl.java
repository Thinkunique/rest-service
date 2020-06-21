package com.app.assignment.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;
import com.app.assignment.model.User;
import com.app.assignment.repo.ItemRepository;
import com.app.assignment.service.CommentService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("cachedThreadPool")
    ExecutorService executor;
	
	@Autowired
	ItemRepository itemRepository;

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

			itemRepository.addItem(childComment.getId(), childComment);
			
			return h;

		},executor);

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
		
		Map<Integer, Integer> sortedByValue = counts.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		List<Integer> itemIds=sortedByValue.keySet().stream().collect(Collectors.toList()).subList(0, 10);
		
		List<Comment> listItem=new ArrayList<>();
		for (Integer i: itemIds) {
			Item item=itemRepository.getItem(String.valueOf(i));	
			
			if(item!=null)
			{
				Future<Comment> futureComment=getComment(item);
				
				Comment comment=null;
				try {
					comment = futureComment.get();
				} catch (InterruptedException | ExecutionException e) {
					System.out.println(e);
				}
				
				listItem.add(comment);
	
				System.out.println("Redis->"+comment);
			}
			else {
				System.out.println("Redis->"+item);
			}
			
		}
		
		System.out.println("count size: "+s.getKids().size());
		System.out.println("counts size: "+counts.size());
		
		
		long lEndTime = System.currentTimeMillis();

        long output = lEndTime - lStartTime;
        
        System.out.println("Elapsed time in milliseconds: " + output/1000);
		
		return listItem;
	}
	
	
	private Future<Comment> getComment(Item item)
	{
		Gson gson = JsonConverter.getGson();
		
		CompletableFuture<Comment> completableFuture = CompletableFuture.supplyAsync(() -> {

			Map m = (Map) restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/user/" + item.getBy() + ".json",
					Object.class);
			new Gson().toJson(m, Map.class);
			User user = gson.fromJson(gson.toJson(m, Map.class), User.class);
			
			Comment comment=new Comment();
			comment.setId(item.getId());
			comment.setText(item.getText());
			
			LocalDate today = LocalDate.now();                      
			LocalDate birthday = Instant.ofEpochMilli(user.getCreated().getTime())
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate();  
			 
			Period p = Period.between(birthday, today);
	
			user.setAge(p.getYears());
			
			comment.setUser(user);
			
			return comment;

		},executor);
		
		return completableFuture;
		
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
