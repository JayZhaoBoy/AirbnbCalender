package com.example.airbnb_calender.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期处理工具类
 * @author Zhoujun
 *	说明：对日期格式的格式化与转换操作等一系列操作
 */
public class DateUtil {
	
	/**
	 * 字符串转换成日期  yyyy-MM-dd HH:mm:ss
	 * @param strFormat  格式定义 如：yyyy-MM-dd HH:mm:ss
	 * @param dateValue 日期对象
	 * @return
	 */
	public static Date stringToDate(String strFormat, String dateValue) {
		if (dateValue == null)
			return null;
		if (strFormat == null)
			strFormat = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;
		try {
			newDate = dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			newDate = null;
		}
		return newDate;
	}
	/**
	 * 将时间秒数或者毫秒数格式
	 * @param strFormat
	 * @param time 时间秒数或者毫秒数
	 * @return
	 */
	public static String getFormatTimeMillis(String strFormat, String time) {
		if (TextUtils.isEmpty(time) || time.equals("0")) {
			return "";
		}
		if (time.length() < 13) {

			time = time + "000";
		}
		String defaultTime;
		try {
			long endDate = Long.parseLong(time);
			Date date = new Date(endDate);
			defaultTime = dateToString(strFormat, date);
		} catch (NumberFormatException e) {
			return "";
		}
		return defaultTime;
	}
	/**
	 * 日期转成字符串
	 * @param strFormat 格式定义 如：yyyy-MM-dd HH:mm:ss
	 * @param date 日期字符串
	 * @return
	 */
	public static String dateToString(String strFormat, Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		return dateFormat.format(date);
	}

	
	/**
	 * 计算两个日期间隔天数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int countDays(Date begin, Date end) {
		int days = 0;
		Calendar c_b = Calendar.getInstance();
		Calendar c_e = Calendar.getInstance();
		c_b.setTime(begin);
		c_e.setTime(end);
		while (c_b.before(c_e)) {
			days++;
			c_b.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}
	public static int countDays(long begin, long end) {
		int days = 0;
		Calendar c_b = Calendar.getInstance();
		Calendar c_e = Calendar.getInstance();
		Date bg = new Date(begin) ;
		Date eg = new Date(end) ;
		c_b.setTime(bg);
		c_e.setTime(eg);
		while (c_b.before(c_e)) {
			days++;
			c_b.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}
	
	/**
	 * 获取最近的时间
	 * @param str1
	 * @return
	 */
	public static String getNearTime(String str1){
			String result = "";
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date one;
	        Date two;
	        long day = 0;  
	        long hour = 0;  
	        long min = 0;  
	        long sec = 0;  
	        try {  
	            one = df.parse(str1);  
	            two = new Date();
	            long time1 = one.getTime();  
	            long time2 = two.getTime();  
	            long diff ;  
	            if(time1<time2) {  
	                diff = time2 - time1;  
	            } else {  
	                diff = time1 - time2;  
	            }  
	            day = diff / (24 * 60 * 60 * 1000);  
	            hour = (diff / (60 * 60 * 1000) - day * 24);  
	            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
	            sec = (diff/1000-day*24*60*60-hour*60*60-min*60); 
	            System.out.println(day+"天"+hour+"小时"+min+"分钟"+sec+"秒");
	            long m = day*24 * 60+hour*60+min;
	            
				if(m<=1){
					result = "一分钟前";
				}else if(m>1 && m<=5){
					result = "五分钟前";
				}else if(m>5 && m<=30){
					result = "半小时前";
				}else if(m>30 && m<=60){
					result = "一小时前";
				}else if(m>60 && m<=60*2){
					result = "两小时前";
				}else if(m>60*2 && m<=60*24){
					result = "一天前";
				}else if(m>60*24 && m<=60*24*2){
					result = "两天前";
				}else if(m>60*24*2 && m<=60*24*7){
					result = "一星期前";
				}else if(m>60*24*7 && m<=60*24*30){
					result = "一个月前";
				}else if(m>60*24*30 && m<=60*24*30*6){
					result = "六个月前";
				}else if(m>60*24*30*6){
					result = "很久以前";
				}
	            day = diff / (24 * 60 * 60 * 1000);  
	            hour = (diff / (60 * 60 * 1000) - day * 24);  
	            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
	            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
	        } catch (ParseException e) {
	        }  
	        return result;  
	    }
	
	/**
	 * 判断是否是下一个月
	 * 
	 * @param clickDate
	 * @param currentYear
	 * @param currentMonth
	 * @return 1 为点击的是下个月，-1点击的是上个月,0为本月
	 */
	public static int isNextMonth(Date clickDate, int currentYear,
                                  int currentMonth) {
		int clickYear = clickDate.getYear() + 1900;
		int clickMonth = clickDate.getMonth();
		if (clickYear > currentYear) {
			return 1;
		} else if (clickYear == currentYear) {
			if (clickMonth > currentMonth) {
				return 1;
			} else if (clickMonth < currentMonth) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}
	/**
	 * 获取星期
	 * @param date
	 * @return
	 */
	public static String getWeek(String date) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek = -1;
		String myWeek = "";
		try {
			Date date1 = formater.parse((String)date);
			Calendar calNow = Calendar.getInstance(Locale.CHINA);
			calNow.setFirstDayOfWeek(Calendar.SUNDAY);
			calNow.setTime(date1);
			dayOfWeek = calNow.get(Calendar.DAY_OF_WEEK);
		} catch (ParseException e) {
		}
		final String dayNames[] = {  "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		if(dayOfWeek>0 && dayOfWeek<8){
			myWeek = dayNames[dayOfWeek-1];
		}
		return myWeek;
	}
}
