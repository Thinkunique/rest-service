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
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.repo.ItemRepository;
import com.app.assignment.service.CommentService;
import com.app.assignment.service.UserService;
import com.app.assignment.util.JsonConverter;
import com.google.gson.Gson;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	HackerNewsProxyService hackerNewsProxyService;
	
	@Autowired
    @Qualifier("cachedThreadPool")
    ExecutorService executor;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	UserService userService;

	public Future<Map<String, List<Integer>>> calculateAsync(int id) throws InterruptedException {

		Map<String, List<Integer>> h = new ConcurrentHashMap<>();

		CompletableFuture<Map<String, List<Integer>>> completableFuture = CompletableFuture.supplyAsync(() -> {

			Item childComment = hackerNewsProxyService.getItem(String.valueOf(id));

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
		
		
		Item s = hackerNewsProxyService.getItem(String.valueOf(id));

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
				Comment comment=getComment(item);
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
	
	
	private Comment getComment(Item item)
	{

		Comment comment=new Comment();
		
		User user=userService.getUserDetails(item.getBy());

		comment.setId(item.getId());
		comment.setText(item.getText());
		comment.setUser(user);
		
		return comment;
		
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
