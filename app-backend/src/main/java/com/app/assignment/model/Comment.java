package com.app.assignment.model;

import java.util.List;

public class Comment {

	private String text;
	
	List<Comment> childComments;
	

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the childComments
	 */
	public List<Comment> getChildComments() {
		return childComments;
	}

	/**
	 * @param childComments the childComments to set
	 */
	public void setChildComments(List<Comment> childComments) {
		this.childComments = childComments;
	}

	
	
	
}
