package com.paypal.adaptive.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * <p>Java class for DateUtil.
 */
public class DateUtil {

	public static String getCurrentDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar now = Calendar.getInstance();
		return sdf.format(now.getTime());
	}

	public static String getDateAfter(int num_of_days) throws Exception {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, num_of_days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(now.getTime());
	}

}


