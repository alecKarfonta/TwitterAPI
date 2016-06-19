package com.alec.TwitterAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeLib {

	private static DateFormat	dateFormat	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String now() {
//		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(new Date());
	}

}
