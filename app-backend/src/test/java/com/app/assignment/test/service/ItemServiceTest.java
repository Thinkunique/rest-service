package com.app.assignment.test.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.assignment.model.Item;
import com.app.assignment.proxy.service.HackerNewsProxyService;
import com.app.assignment.service.ItemService;
import com.app.assignment.test.configuration.AppTestConfiguration;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTestConfiguration.class)
public class ItemServiceTest {

	@Autowired
	HackerNewsProxyService hackerNewsProxyService; 
	
	@Autowired
	ItemService itemService;

	@Test
	public void getCommentItem() {
		Item item=getItem();
		Mockito.when(hackerNewsProxyService.getItem("1")).thenReturn(item);
		assertEquals(itemService.getItem("1"),item);
	}
	
	private Item getItem()
	{
		Item item=new Item();
		item.setId("1");
		item.setBy("sdsd");
		item.setScore(5454);
		return item;
	}

}
