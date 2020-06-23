package com.app.assignment.test.service;


import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.assignment.model.Comment;
import com.app.assignment.model.Item;
import com.app.assignment.model.User;
import com.app.assignment.service.CommentService;
import com.app.assignment.service.UserService;
import com.app.assignment.test.configuration.AppTestConfiguration;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTestConfiguration.class)
public class CommentServiceTest {

	@Autowired
	private CommentService commentService;
	
	@Autowired
    private UserService userService;
 
    @Test
    public void whenCommentUsernameIsProvided_thenRetrievedCommentUserIdIsCorrect() {
    	Item item=getCommentItem();
    	User user=getUser();
    	Mockito.when(userService.getUserDetails(item.getBy())).thenReturn(user);
    	Comment comment=commentService.getComment(item);
    	assertEquals(user.getId(),comment.getUser().getId());
    }
    
    private Item getCommentItem()
    {
    	Item item=new Item();
    	item.setBy("xnbh");
    	item.setText("This is a test comment");
    	item.setId("212");
    	return item;
    }
    
    private User getUser()
    {
    	User user=new User();
    	user.setId("1212");
    	user.setAge(21);
    	return user;
    }
	
}
