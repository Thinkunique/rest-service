package com.app.assignment.service;

import java.util.List;

import com.app.assignment.model.Comment;

public interface CommentService {

	public List<Comment> getTopComments(int id) throws InterruptedException;
	
}
