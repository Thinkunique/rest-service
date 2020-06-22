package com.app.assignment.service.impl;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;
import com.app.assignment.model.User;
import com.app.assignment.repo.ItemRepository;
import com.app.assignment.service.CommentService;
import com.app.assignment.service.ItemService;
import com.app.assignment.service.UserService;

@Service
public class CommentServiceImpl implements CommentService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	UserService userService;

	@Autowired
	ItemService itemService;

	@Autowired
	@Qualifier("cachedThreadPool")
	ExecutorService executor;

	@Autowired
	ItemRepository itemRepository;

	@Override
	public List<Comment> getTopComments(int id) throws InterruptedException {
		logger.info("Enter: CommentServiceImpl.getTopComments-[{}]", id);
		Item s = itemService.getItem(String.valueOf(id));
		Map<Integer, Integer> counts = new ConcurrentHashMap<>();
		CountDownLatch latch = new CountDownLatch(s.getKids().size());
		for (Integer i : s.getKids()) {
			executor.submit(() -> {
				int count = 0;
				count += getTotalChildComments(i);
				counts.put(i, count);
				latch.countDown();
			});

		}
		latch.await();
		Map<Integer, Integer> sortedByValue = counts.entrySet().stream()
				.sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		List<Integer> itemIds = sortedByValue.keySet().stream().collect(Collectors.toList()).subList(0, 10);
		List<Comment> listItem = new ArrayList<>();
		for (Integer i : itemIds) {
			Item item = itemRepository.getItem(String.valueOf(i));
			if (item != null) {
				Comment comment = getComment(item);
				listItem.add(comment);
			}
		}
		logger.info("Exit: CommentServiceImpl.getTopComments-[{}]", id);
		return listItem;
	}

	@Override
	public Comment getComment(Item item) {
		logger.info("Enter: CommentServiceImpl.getComment-[{}]", item.getId());
		Comment comment = new Comment();
		User user = userService.getUserDetails(item.getBy());
		comment.setId(item.getId());
		comment.setText(item.getText());
		comment.setUser(user);
		logger.info("Exit: CommentServiceImpl.getComment-[{}]", item.getId());
		return comment;

	}

	@Override
	public int getTotalChildComments(int id) {
		logger.info("Enter: CommentServiceImpl.getTotalChildComments-[{}]", id);
		try {
			Future<Map<String, List<Integer>>> future = itemService.getItemKids(id);
			Map<String, List<Integer>> h = future.get();
			int c = 0;
			if (h.get(String.valueOf(id)) != null && !h.get(String.valueOf(id)).isEmpty()) {
				c += h.get(String.valueOf(id)).size();
				for (Integer i : h.get(String.valueOf(id))) {
					c += getTotalChildComments(i);
				}
				return c;
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("CommentServiceImpl.getTotalChildComments-[{}]", id, e);
		}
		logger.info("Exit: CommentServiceImpl.getTotalChildComments-[{}]", id);
		return 0;

	}

}
