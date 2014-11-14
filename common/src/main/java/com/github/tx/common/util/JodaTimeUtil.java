package com.github.tx.common.util;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeUtil {
	
	public static final String defaultPattern = "yyyy-MM-dd HH:mm:ss";
	
	public static final String simplePattern = "yyyy-MM-dd";
	
	/**
	 * 日期转字符串<br>
	 * 默认格式:yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String convertToString(Date date) {
		DateTime dt = new DateTime(date);
		return dt.toString(defaultPattern);
	}
	
	/**
	 * 日期转字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertToString(Date date, String pattern) {
		DateTime dt = new DateTime(date);
		return dt.toString(pattern);
	}
	
	/**
	 * 字符串转日期<br>
	 * 默认格式:yyyy-MM-dd HH:mm:ss
	 * @param dateString
	 * @return
	 */
	public static Date convertFromString(String dateString) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(defaultPattern);
		DateTime dt = fmt.parseDateTime(dateString);
		return dt.toDate();
	}
	
	/**
	 * 字符串转日期
	 * @param dateString
	 * @param pattern
	 * @return
	 */
	public static Date convertFromString(String dateString, String pattern) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
		DateTime dt = fmt.parseDateTime(dateString);
		return dt.toDate();
	}
	
	/**
	 * 获取当前日期
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		DateTime dt = new DateTime(new Date());
		return dt.toString(pattern);
	}
	
	/**
	 * 获取当前日期<br>
	 * 默认格式:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime() {
		DateTime dt = new DateTime(new Date());
		return dt.toString(defaultPattern);
	}
	
	/**
	 * 获取当前日期<br>
	 * 默认格式:yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentDate() {
		DateTime dt = new DateTime(new Date());
		return dt.toString(simplePattern);
	}
	
	/**
	 * 将unix时间戳转字符串
	 * @param timeMill 时间戳
	 * @param pattern 时间格式
	 * @return
	 */
	public static String convertTimeMillToString(long timeMill, String pattern) {
		DateTime dt = new DateTime(timeMill);
		return dt.toString(pattern);
	}
	 
}
