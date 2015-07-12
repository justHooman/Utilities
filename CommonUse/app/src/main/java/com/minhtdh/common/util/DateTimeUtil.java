package com.minhtdh.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
	/**
	 * get current time in format yyyyMMddHHmmss 
	 * @author MinhTDH
	 * @return String
	 *********************************************************
	 */
	public static String getStringLongTimeFromNow() {
		return getDateTimeFormat(System.currentTimeMillis(), "yyyyMMddHHmmss");
	}
	/**
	 * get current date time and display as passing format
	 * @author MinhTDH
	 * @param time_milis
	 * @param format
	 * @return String
	 *********************************************************
	 */
	public static String getDateTimeFormat(long time_milis, String format) {
		String time="";
		Date date=new Date(time_milis);
		try {
			SimpleDateFormat SDF = new SimpleDateFormat(format, Locale.US);
			time = SDF.format(date);
		} catch (Exception e) {
		}
		return time;
	}
	/**
	 * get date time for mat dd/MM/yyyy
	 * @author MinhTDH
	 * @param time
	 * @return String
	 *********************************************************
	 */
	public static String getDateTimeFormatToServer(long time) {
		return getDateTimeFormat(time, "dd/MM/yyyy");
	}
	
	public static String getDateTimeFormatGMT(String format) {
		Date date = new Date();
		return getDateTimeFormatGMT(format, date);
	}
	
	public static String getDateTimeFormatGMT(String format, long time) {
		Date date = new Date(time);
		return getDateTimeFormatGMT(format, date);
	}
	
	public static String getDateTimeFormatGMT(String format, Date date) {
		String ret = "";
	    DateFormat gmtFormat = new SimpleDateFormat(format, Locale.US);
	    TimeZone gmtTime = TimeZone.getTimeZone("GMT");
	    gmtFormat.setTimeZone(gmtTime);
	    ret = gmtFormat.format(date);
	    return ret;
	}
	/**
	 * convert string time in format dd/MM/yyyy to Date object
	 * @author MinhTDH
	 * @param input
	 * @return Date
	 *********************************************************
	 */
	public static Date convertStringtoDate(String input) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date ret = null;
		if (input != null) {
			try {
				ret = sdf.parse(input);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
