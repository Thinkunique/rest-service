package com.app.assignment.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.assignment.exception.CommentNotFoundException;
import com.app.assignment.exception.ItemNotFoundException;
import com.app.assignment.exception.RedisException;
import com.app.assignment.exception.StoryNotFoundException;
import com.app.assignment.exception.ThreadExecutionException;
import com.app.assignment.exception.UserNotFoundException;
import com.app.assignment.response.CommentResponse;
import com.app.assignment.response.ItemResponse;
import com.app.assignment.response.StoryResponse;
import com.app.assignment.response.UserResponse;

@RestControllerAdvice
public class AppControllerExceptionHandler {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ItemResponse> handleItemNotFoundException(ItemNotFoundException ex) {
		ItemResponse responseMsg = new ItemResponse(ex.getMessage());
		return new ResponseEntity<ItemResponse>(responseMsg,HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<UserResponse> handleUserNotFoundException(UserNotFoundException ex) {
		UserResponse responseMsg = new UserResponse(ex.getMessage());
		return  new ResponseEntity<UserResponse>(responseMsg,HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(StoryNotFoundException.class)
	public ResponseEntity<StoryResponse> handleStoryNotFoundException(StoryNotFoundException ex) {
		StoryResponse responseMsg = new StoryResponse(ex.getMessage());
		return new ResponseEntity<StoryResponse>(responseMsg,HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<CommentResponse> handleCommentNotFoundException(CommentNotFoundException ex) {
		CommentResponse responseMsg = new CommentResponse(ex.getMessage());
		return new ResponseEntity<CommentResponse>(responseMsg,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ThreadExecutionException.class)
	public ResponseEntity<ItemResponse> handleThreadExecutionException(ThreadExecutionException ex) {
		ItemResponse responseMsg = new ItemResponse(ex.getMessage());
		return new ResponseEntity<ItemResponse>(responseMsg,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RedisException.class)
	public ResponseEntity<ItemResponse> handleRedisException(RedisException ex) {
		ItemResponse responseMsg = new ItemResponse(ex.getMessage());
		return new ResponseEntity<ItemResponse>(responseMsg,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
