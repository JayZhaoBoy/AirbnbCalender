package com.example.airbnb_calender.model;

import com.example.airbnb_calender.util.CalendarUtils;
import com.example.airbnb_calender.util.CalenderHasRoomTypeHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by jayzhao on 2018/1/11.
 */

public class DateRange {
    protected Calendar startDate;
    protected Calendar endDate;
    protected Calendar toDay;//今日
    protected boolean isSelectFuture = true;//只能选择未来日期
    protected int daySelectOffset = 1;//选择时候日期偏移

    public List<List<String>> getHasBeenSelectedDays() {
        return mHasBeenSelectedDays;
    }

    public void setHasBeenSelectedDays(List<List<String>> hasBeenSelectedDays) {
        mHasBeenSelectedDays = hasBeenSelectedDays;
    }

    private List<List<String>> mHasBeenSelectedDays;

    public DateRange(boolean isSelectFuture, int daySelectOffset) {
        this.isSelectFuture = isSelectFuture;
        this.daySelectOffset = daySelectOffset;
        toDay = CalendarUtils.getToDay();
    }

    protected boolean isSelectEnable(Calendar day) {
        if (isSelectFuture) {
            if (day.before(toDay)) {
                return false;
            }
        } else {
            if (day.after(toDay)) {
                return false;
            }
        }
        return true;
    }

    //返回true代表需要刷新视图
    public boolean selectDay(CalendarDayModel calendarDayModel) {
        if (calendarDayModel == null){
            return true;
        }
        Calendar day = calendarDayModel.dayCalendar;
        if (day == null) {
            startDate = endDate = null;
            return true;
        }
        if (!isSelectEnable(day)) {
            return false;
        }
        if (startDate == null) {
            if (CalenderHasRoomTypeHelper.MODEL != null && CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart){
                CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart = false;
                CalenderHasRoomTypeHelper.MODEL.isHasBeenChose = true;
            }
            CalenderHasRoomTypeHelper.START_ISNOT_NULL = true;
            startDate = day;
            CalenderHasRoomTypeHelper.START_CALENDER = startDate;
        } else if (endDate == null) {
            if (day.after(startDate)) {
                endDate = day;
                if (endDate.equals(CalenderHasRoomTypeHelper.FIRST_SELECTED_BEHIND_START)){
                    CalenderHasRoomTypeHelper.MODEL = calendarDayModel;
                    CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart = true;
                }
                CalenderHasRoomTypeHelper.FIRST_SELECTED_BEHIND_START = null;
            } else if (day.equals(startDate)) {//重复点击
                CalenderHasRoomTypeHelper.FIRST_SELECTED_BEHIND_START = null;
                startDate = null;
            } else {
                startDate = day;
                if (CalenderHasRoomTypeHelper.MODEL != null && CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart){
                    CalenderHasRoomTypeHelper.MODEL.isHasBeenChose = true;
                    CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart = false;
                }
                CalenderHasRoomTypeHelper.START_CALENDER = startDate;
                CalenderHasRoomTypeHelper.START_ISNOT_NULL = true;
                CalenderHasRoomTypeHelper.FIRST_SELECTED_BEHIND_START = null;
            }
        } else {
            startDate = day;
            if (CalenderHasRoomTypeHelper.MODEL != null && CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart) {
                CalenderHasRoomTypeHelper.MODEL.isHasBeenChose = true;
                CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart = false;
            }
            CalenderHasRoomTypeHelper.START_CALENDER = startDate;
            CalenderHasRoomTypeHelper.FIRST_SELECTED_BEHIND_START = null;
            CalenderHasRoomTypeHelper.START_ISNOT_NULL = true;
            endDate = null;
        }
        return true;
    }

    private boolean hasSelectedStartAndEnd() {
        return startDate != null && endDate != null;
    }

    public boolean clickDay(CalendarDayModel day, List<CalendarMonthModel> list) {
        if (selectDay(day)) {
            for (CalendarMonthModel calendarMonthModel : list) {
                changeMonth(calendarMonthModel);
            }
            return true;
        }
        return false;
    }

    public void changeMonth(CalendarMonthModel monthModel) {
        monthModel.setHasSelectedStartAndEnd(hasSelectedStartAndEnd());
        boolean unSelcted = false;
        if (startDate == null) {
            unSelcted = true;
        }
        for (CalendarDayModel calendarDayModel : monthModel.getDays()) {
            Calendar d = calendarDayModel.dayCalendar;
            if (d.equals(endDate) && calendarDayModel.isHasBeenChose){
                CalenderHasRoomTypeHelper.MODEL = calendarDayModel;
                calendarDayModel.isHasBeenChose = false;
                calendarDayModel.isCanNotBeStart = true;
            }
            if (isSelectFuture) {
                calendarDayModel.isUnavailable = d.before(toDay);
            } else {
                calendarDayModel.isUnavailable = d.after(toDay);
            }
            if (calendarDayModel.isUnavailable) {
                continue;
            }
            calendarDayModel.unSelected();//重置选中状态
            if (unSelcted) {
                continue;
            }
            if (endDate == null) {
                if (startDate.equals(d)) {
                    calendarDayModel.isSelectedStartDay = true;
                }
            } else {
                if (d.before(startDate)) {
                    calendarDayModel.unSelected();
                } else if (d.equals(startDate)) {
                    calendarDayModel.isSelectedStartDay = true;
                } else if (d.before(endDate)) {
                    calendarDayModel.isBetweenStartAndEndSelected = true;
                } else if (d.equals(endDate)) {
                    calendarDayModel.isSelectedEndDay = true;
                } else {
                    calendarDayModel.unSelected();
                }
            }
        }
    }

    public boolean isSelectFuture() {
        return isSelectFuture;
    }

    public void setSelectFuture(boolean selectFuture) {
        isSelectFuture = selectFuture;
    }

    public int getDaySelectOffset() {
        return daySelectOffset;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getToDay() {
        return toDay;
    }

    public void setToDay(Calendar toDay) {
        this.toDay = toDay;
    }

    public void setDaySelectOffset(int daySelectOffset) {
        this.daySelectOffset = daySelectOffset;
    }
}

