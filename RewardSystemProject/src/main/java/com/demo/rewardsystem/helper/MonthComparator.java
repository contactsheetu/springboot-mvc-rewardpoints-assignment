package com.demo.rewardsystem.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class MonthComparator<T> implements Comparator<String> {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM");

	@Override
	public int compare(String monthName1, String monthName2) {
		try {
			Date date1 = sdf.parse(monthName1);
			Date date2 = sdf.parse(monthName2);

			return date1.compareTo(date2);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return 0;
	}
}
