package com.app.assignment.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.assignment.exception.CommentNotFoundException;
import com.app.assignment.exception.ItemNotFoundException;
import com.app.assignment.exception.StoryNotFoundException;
import com.app.assignment.exception.UserNotFoundException;
import com.app.assignment.response.CommentResponse;
import com.app.assignment.response.ItemResponse;
import com.app.assignment.response.StoryResponse;
import com.app.assignment.response.UserResponse;

@RestControllerAdvice
public class AppControllerExceptiionHandler {

	@ExceptionHandler(ItemNotFoundException.class)
	public ItemResponse handleItemNotFoundException(ItemNotFoundException ex) {
		ItemResponse responseMsg = new ItemResponse(ex.getMessage());
		return responseMsg;
	}

	@ExceptionHandler(UserNotFoundException.class)
	public UserResponse handleUserNotFoundException(UserNotFoundException ex) {
		UserResponse responseMsg = new UserResponse(ex.getMessage());
		return responseMsg;
	}

	@ExceptionHandler(StoryNotFoundException.class)
	public StoryResponse handleStoryNotFoundException(StoryNotFoundException ex) {
		StoryResponse responseMsg = new StoryResponse(ex.getMessage());
		return responseMsg;
	}

	@ExceptionHandler(CommentNotFoundException.class)
	public CommentResponse handleCommentNotFoundException(CommentNotFoundException ex) {
		CommentResponse responseMsg = new CommentResponse(ex.getMessage());
		return responseMsg;
	}
}
