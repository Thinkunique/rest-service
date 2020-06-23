package com.app.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.app.assignment.exception.CommentNotFoundException;
import com.app.assignment.exception.ItemNotFoundException;
import com.app.assignment.exception.StoryNotFoundException;
import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;
import com.app.assignment.model.User;
import com.app.assignment.repo.ItemRepository;
import com.app.assignment.service.CommentService;
import com.app.assignment.service.ItemService;
import com.app.assignment.service.UserService;
import com.app.assignment.util.ListUtility;
import com.app.assignment.util.MapUtility;

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
	public List<Comment> getTopComments(int storyId) throws InterruptedException {
		logger.info("Enter: CommentServiceImpl.getTopComments-[{}]", storyId);
		List<Comment> parentCommentList = new ArrayList<>();
		Map<Integer, Integer> childCommentsCount = new ConcurrentHashMap<>();
		Item storyItem = itemService.getItem(String.valueOf(storyId));
		executeChildTasks(storyItem, childCommentsCount);
		Map<Integer, Integer> sortedChildCommentsCount = MapUtility.sortByValueDesc(childCommentsCount);
		List<Integer> topParentCommentIdList = ListUtility.convertMapKeysToList(sortedChildCommentsCount).subList(0,
				10);
		for (Integer i : topParentCommentIdList) {
			Item item = itemRepository.getItem(String.valueOf(i));
			if (item != null) {
				Comment comment = getComment(item);
				parentCommentList.add(comment);
			}
		}
		logger.info("Exit: CommentServiceImpl.getTopComments-[{}]", storyId);
		return parentCommentList;
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
			int count = 0;
			Future<Map<String, List<Integer>>> future = itemService.getItemKids(id);
			Map<String, List<Integer>> itemKids = future.get();
			List<Integer> kids = itemKids.get(String.valueOf(id));
			if (kids != null && !kids.isEmpty()) {
				count += kids.size();
				for (Integer i : kids) {
					count += getTotalChildComments(i);
				}
				logger.info("Exit: CommentServiceImpl.getTotalChildComments-[{}]", id);
				return count;
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("CommentServiceImpl.getTotalChildComments-[{}]", id, e);
		}
		logger.info("Exit: CommentServiceImpl.getTotalChildComments-[{}]", id);
		return 0;
	}

	/**
	 * Store count of total number of child comments in childCommentsCount
	 * 
	 * @param story
	 * @param childCommentsCount
	 */
	private void executeChildTasks(Item story, Map<Integer, Integer> childCommentsCount) {
		
		if(story.getKids()==null||story.getKids().isEmpty())
		{
			throw new CommentNotFoundException("There are no comments on given story: "+story.getId());
		}
		
		CountDownLatch latch = new CountDownLatch(story.getKids().size());
		for (Integer kid : story.getKids()) {
			executor.submit(() -> {
				int count = getTotalChildComments(kid);
				childCommentsCount.put(kid, count);
				latch.countDown();
			});

		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("CommentServiceImpl.executeChildTasks ", e);
		}
	}

}
