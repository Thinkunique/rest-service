package com.app.assignment.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class DateUtility {

	public static int getAge(long dob)
	{
		LocalDate today = LocalDate.now();
		LocalDate birthday = Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault())
				.toLocalDate();
		Period p = Period.between(birthday, today);
		return p.getYears();
	}
	
}
