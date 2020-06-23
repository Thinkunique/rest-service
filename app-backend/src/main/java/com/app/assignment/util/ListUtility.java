package com.app.assignment.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.app.assignment.model.Story;

public class ListUtility {

	public static List<Integer> convertMapKeysToList(Map<Integer, Integer> map) {
		return map.keySet().stream().collect(Collectors.toList());
	}

	public static List<Story> sortStoryListByScoreDesc(List<Story> list) {
		return list.stream().sorted(Comparator.comparingInt(Story::getScore).reversed()).collect(Collectors.toList());
	}
	
	public static List<Story> subListByRange(List<Story> list,int start,int end) {
		return list.subList(start, end);
	}

}
