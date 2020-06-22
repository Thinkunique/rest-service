package com.app.assignment.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListUtility {

	
	public static List<Integer> convertMapKeysToList(Map<Integer,Integer> map)
	{
		return map.keySet().stream().collect(Collectors.toList());
	}
	
}
