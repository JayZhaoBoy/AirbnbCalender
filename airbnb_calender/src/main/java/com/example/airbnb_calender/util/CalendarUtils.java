package com.example.airbnb_calender.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jayzhao on 2018/1/11.
 */

public class CalendarUtils {
    public static Calendar getToDay() {
        Calendar toDay = Calendar.getInstance();
        int year = toDay.get(Calendar.YEAR);
        int month = toDay.get(Calendar.MONTH);
        int day = toDay.get(Calendar.DAY_OF_MONTH);
        toDay.clear();
        toDay.set(Calendar.YEAR, year);
        toDay.set(Calendar.MONTH, month);
        toDay.set(Calendar.DAY_OF_MONTH, day);
        return toDay;
    }

    public static Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    //或者这个月的天数
    public static int getMaxMonthCount(Calendar monthDay) {
        return monthDay.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

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
}
