package com.app.assignment.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.app.assignment.model.Item;
import com.app.assignment.repo.ItemRepository;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

	@Autowired
	RedisTemplate<String, Item> redisTemplate;

	@Override
	public void addItem(String id, Item item) {
		try {
			redisTemplate.opsForHash().put("ITEM", id, item);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public Item getItem(String id) {
		 return (Item) redisTemplate.opsForHash().get("ITEM", id);
	}

}
