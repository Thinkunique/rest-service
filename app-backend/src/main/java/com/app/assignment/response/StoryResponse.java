package com.app.assignment.response;

import java.util.List;

import com.app.assignment.model.Story;

public class StoryResponse {

	String message;

	int total;

	List<Story> stories;
	
	/**
	 * @param message
	 */
	public StoryResponse(String message) {
		this.message = message;
	}
	
	

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}



	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the stories
	 */
	public List<Story> getStories() {
		return stories;
	}

	/**
	 * @param stories the stories to set
	 */
	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

}
