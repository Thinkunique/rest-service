package com.app.assignment.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.app.assignment.model.Item;

public interface ItemService {
	
	public Item getItem(String id);

	public Future<Map<String, List<Integer>>> getItemKids(int id) throws InterruptedException;
	
}
