package com.app.assignment.service;

import java.util.List;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;

public interface CommentService {

	public List<Comment> getTopComments(int id) throws InterruptedException;
	
	public Comment getComment(Item item);
	
	public int getTotalChildComments(int id);
	
}
