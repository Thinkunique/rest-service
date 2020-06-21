package com.app.assignment.repo;

import com.app.assignment.model.Item;

public interface ItemRepository {

	public void addItem(String id,Item item);
	
	public Item getItem(String id);
	
}
