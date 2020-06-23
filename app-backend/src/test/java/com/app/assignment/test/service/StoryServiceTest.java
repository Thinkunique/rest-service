package com.app.assignment.test.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.assignment.model.Story;
import com.app.assignment.service.StoryService;
import com.app.assignment.test.configuration.AppTestConfiguration;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTestConfiguration.class)
public class StoryServiceTest {
	
	@Autowired
	private StoryService storyService;
	
	@Test
	public void sortTopStories() {
		List<Story> sortedList=storyService.sortTopStories(getStoryList());
		assertEquals(sortedList.get(0).getScore(),21);
		assertEquals(sortedList.get(2).getScore(),10);
	}
	
	private List<Story> getStoryList()
	{
		List<Story> list=new ArrayList<>();
		list.add(getStoryWithScore(10));
		list.add(getStoryWithScore(20));
		list.add(getStoryWithScore(21));
		return list;
	}
	
	private Story getStoryWithScore(int score)
	{
		Story story=new Story();
		story.setScore(score);
		return story;
	}
	
}
