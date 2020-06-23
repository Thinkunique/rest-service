package com.app.assignment.repo.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.app.assignment.exception.RedisException;
import com.app.assignment.model.Item;
import com.app.assignment.repo.ItemRepository;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	RedisTemplate<String, Item> redisTemplate;

	/**
	 * 
	 * This method saves the item data in redis datastore
	 * 
	 */
	@Override
	public void addItem(String id, Item item) {
		try {
			redisTemplate.opsForHash().put("ITEM", id, item);
		} catch (Exception e) {
			logger.error("Error while saving in redis", e);
			throw new RedisException(e.getMessage());
		}
	}

	/**
	 * 
	 * This method retrieves the item data from redis datastore
	 * 
	 */
	@Override
	public Item getItem(String id) {
		Item item = null;
		try {
			item = (Item) redisTemplate.opsForHash().get("ITEM", id);
		} catch (Exception e) {
			logger.error("Error while fetching item from redis", e);
			throw new RedisException(e.getMessage());
		}
		return item;
	}

}
