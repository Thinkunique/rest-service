package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.app.assignment.exception.CommentNotFoundException;
import com.app.assignment.exception.ItemNotFoundException;
import com.app.assignment.model.Item;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.repo.ItemRepository;
import com.app.assignment.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	HackerNewsProxyService hackerNewsProxyService;

	@Autowired
	@Qualifier("cachedThreadPool")
	ExecutorService executor;

	@Autowired
	ItemRepository itemRepository;

	/**
	 *  This method retrieves the comment using id 
	 *  and saves it to Redis datastore and 
	 *  returns Future of count of kids of retrieved comment. 
	 *   
	 * */
	@Override
	public Future<Map<String, List<Integer>>> getItemKids(int id) throws InterruptedException {
		logger.info("Enter: ItemServiceImpl.getItemKids-[{}]", id);
		Map<String, List<Integer>> kidCount = new ConcurrentHashMap<>();
		CompletableFuture<Map<String, List<Integer>>> completableFuture = CompletableFuture.supplyAsync(() -> {
			Item childComment = getChildCommentItem(id);
			if (childComment.getId() != null && childComment.getKids() != null) {
				kidCount.put(childComment.getId(), childComment.getKids());
			} else if (childComment.getId() != null && childComment.getKids() == null) {
				kidCount.put(childComment.getId(), new ArrayList<>());
			}
			itemRepository.addItem(childComment.getId(), childComment);
			return kidCount;
		}, executor);
		logger.info("Enter: ItemServiceImpl.getItemKids-[{}]", id);
		return completableFuture;
	}
	
	
	/**
	 *  This method retrieves the item from hacker news api. 
	 *   
	 * */
	@Override
	public Item getItem(String id) {
		logger.info("Enter: ItemServiceImpl.getItem-[{}]", id);
		Item item = hackerNewsProxyService.getItem(String.valueOf(id));
		logger.info("Enter: ItemServiceImpl.getItem-[{}]", id);
		return item;
	}

	private Item getChildCommentItem(int id) {
		Item childComment = null;
		try {
			childComment = hackerNewsProxyService.getItem(String.valueOf(id));
		} catch (ItemNotFoundException e) {
			throw new CommentNotFoundException("Comment Not Found: " + id);
		}
		return childComment;
	}

}
