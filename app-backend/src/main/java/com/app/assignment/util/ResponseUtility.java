package com.app.assignment.util;

import java.util.List;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Story;
import com.app.assignment.response.CommentResponse;
import com.app.assignment.response.StoryResponse;
import com.app.assignment.response.constants.ResponseConstant;

public class ResponseUtility {

	public static StoryResponse createTopStoryResponse(List<Story> list)
	{
		StoryResponse response = new StoryResponse(ResponseConstant.TOP_COMMENTS_RESPONSE_MSG);		
		response.setTotal(list.size());
		response.setStories(list);
		return response;
	}
	
	public static CommentResponse createCommentResponse(List<Comment> list)
	{
		CommentResponse response = new CommentResponse(ResponseConstant.TOP_COMMENTS_RESPONSE_MSG);
		response.setTotal(list.size());
		response.setComments(list);
		return response;
	}
	
	public static StoryResponse createPastStoryResponse(List<Story> list)
	{
		StoryResponse response = new StoryResponse(ResponseConstant.PAST_TOP_STORIES_RESPONSE_MSG);		
		response.setTotal(list.size());
		response.setStories(list);
		return response;
	}
	
}
