package com.example.airbnb_calender.model;

import com.example.airbnb_calender.util.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jayzhao on 2018/1/11.
 */

public class CalendarMonthModel {
    private int monthDayCount;//这个月多少天
    private Calendar monthCalendar;
    private int dayOffset;
    private List<CalendarDayModel> days;
    private boolean hasSelectedStartAndEnd;//是否同时选中首尾
    private int month;
    private int year;

    public List<String> getHasBeenChoosedList() {
        return hasBeenChoosedList;
    }

    public void setHasBeenChoosedList(List<String> hasBeenChoosedList) {
        this.hasBeenChoosedList = hasBeenChoosedList;
        init(monthCalendar, hasBeenChoosedList);
    }

    private List<String> hasBeenChoosedList;

    public CalendarMonthModel(int year, int month) {
        this(CalendarUtils.getCalendar(year, month, 1), null);
    }

    public CalendarMonthModel(Calendar monthCalendar, List<String> hasBeenChoosedList) {
        this.monthCalendar = monthCalendar;
        init(monthCalendar, hasBeenChoosedList);
    }

    private void init(Calendar monthCalendar, List<String> hasBeenChoosedList) {
        this.year = monthCalendar.get(Calendar.YEAR);
        this.month = monthCalendar.get(Calendar.MONTH);
        dayOffset = findDayOffset();
        Calendar toDay = CalendarUtils.getToDay();
        monthDayCount = CalendarUtils.getMaxMonthCount(monthCalendar);//这个月多少天
        days = new ArrayList(monthDayCount);
        for (int i = 1; i <= monthDayCount; i++) {
            Calendar dayCalendar = CalendarUtils.getCalendar(year, month, i);
            CalendarDayModel day = new CalendarDayModel(i, dayCalendar);
            if (toDay.equals(dayCalendar)) {
                day.isToday = true;
            } else if (toDay.after(dayCalendar)) {
                day.isInThePast = true;
            } else {
                day.isInTheFuture = true;
            }
            if (hasBeenChoosedList != null && hasBeenChoosedList.size() > 0) {
                for (String temps : hasBeenChoosedList) {
                    Calendar dayCalendar_sel = CalendarUtils.getCalendar(year, month, Integer.valueOf(temps));
                    if (dayCalendar_sel.equals(dayCalendar)) {
                        day.isHasBeenChose = true;
                    }
                }
            }
            days.add(day);
        }
    }

    private int findDayOffset() {
        int i = this.monthCalendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = this.monthCalendar.getFirstDayOfWeek();
        if (i < firstDayOfWeek) {
            i += 7;
        }
        return i - firstDayOfWeek;
    }

    public int getCurrentMonth() {
        return this.month;
    }

    public int getCurrentYear() {
        return this.year;
    }

    public CalendarDayModel getDayModel(int day) {
        if (day <= 0 || day > this.getNumberDaysInMonth()) {
            return null;
        } else {
            return getDays().get(day - 1);
        }
    }

    public int getDayOffset() {
        return this.dayOffset;
    }

    public List<CalendarDayModel> getDays() {
        return this.days;
    }

    public String getMonthText() {
        return year + "年" + (month + 1) + "月";
    }

    public int getNumberDaysInMonth() {
        return this.days.size();
    }

    public void setHasSelectedStartAndEnd(boolean hasSelectedStartAndEnd) {
        this.hasSelectedStartAndEnd = hasSelectedStartAndEnd;
    }

    public boolean hasSelectedStartAndEnd() {
        return this.hasSelectedStartAndEnd;
    }


}
