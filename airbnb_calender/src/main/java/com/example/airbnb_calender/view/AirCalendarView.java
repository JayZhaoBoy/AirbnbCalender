package com.example.airbnb_calender.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.airbnb_calender.R;
import com.example.airbnb_calender.model.DateRange;
import com.example.airbnb_calender.util.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jayzhao on 2018/1/11.
 */

public class AirCalendarView extends FrameLayout {
    public interface OnSelectedDayListener {
        void onDaySelected(Calendar startDay, Calendar endDay);
    }

    private RecyclerView mRecyclerView;
    private Calendar toDay;
    private MonthAdapter mMonthAdapter;
    private DateRange dataRange;
    private int toDayYear;
    private int toDayMonth;
    private int toDayPosition;//今天这个月的position
    private TextView startDateTv, endDateTv;
    private List<List<String>> mHasBeenSelectedDays;
    private int visiableYearCount = 1;

    public AirCalendarView(Context context) {
        super(context);
        init(null);
    }

    public AirCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AirCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AirCalendarView);
        visiableYearCount = typedArray.getInt(R.styleable.AirCalendarView_visibleYearCount, 1);
        int daySelectOffset = typedArray.getInt(R.styleable.AirCalendarView_daySelectOffset, 1);
        boolean isSelectFuture = typedArray.getBoolean(R.styleable.AirCalendarView_isSelectFuture, true);
        typedArray.recycle();
        dataRange = new DateRange(isSelectFuture, daySelectOffset);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_aircalendarview, this, false);
        addView(rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.view_aircalendar_rv_date);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        toDay = CalendarUtils.getToDay();
        toDayYear = toDay.get(Calendar.YEAR);
        toDayMonth = toDay.get(Calendar.MONTH);
        List<Calendar> years = new ArrayList<>();
        years.addAll(getYearMonths(toDayYear, toDayMonth));
        mMonthAdapter = new MonthAdapter(years, dataRange, mRecyclerView);
        mRecyclerView.setAdapter(mMonthAdapter);
        mRecyclerView.scrollToPosition(toDayPosition);
    }

    private List<Calendar> getYearMonths(int year, int month) {
        List<Calendar> months = new ArrayList<>();
        for (int i = 0; i < 12 * visiableYearCount; i++) {
            if (month > 11){
                year += 1;
                month = 0;
            }
            Calendar calendar = CalendarUtils.getCalendar(year, month, 1);
            month += 1;
            months.add(calendar);
        }
        return months;
    }


    public void setDefaultDateSel_str(String start, String end){
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)){
            return;
        }else {
            Date dateStart = CalendarUtils.stringToDate("yyyy-MM-dd", start);
            Date dateEnd = CalendarUtils.stringToDate("yyyy-MM-dd", end);
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(dateStart);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(dateEnd);
            setDefaultDateSel(calendarStart, calendarEnd);
        }
    }


    public void setDefaultDateSel(Calendar start, Calendar end){
        dataRange.setStartDate(start);
        dataRange.setEndDate(end);

    }

    public void setHasBeenSelected(List<List<String>> hasBeenSelected) {
        mHasBeenSelectedDays = hasBeenSelected;
        dataRange.setHasBeenSelectedDays(mHasBeenSelectedDays);
    }

    public Calendar getStartDate() {
        return mMonthAdapter.getDateRange().getStartDate();
    }

    public Calendar getEndDate() {
        return mMonthAdapter.getDateRange().getEndDate();
    }

    public void clearSelect() {
        mMonthAdapter.clearSelect();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public boolean isSelectFuture() {
        return dataRange.isSelectFuture();
    }

    public int getDaySelectOffset() {
        return dataRange.getDaySelectOffset();
    }

    public OnSelectedDayListener getOnSelectedDayListener() {
        return mMonthAdapter.getOnSelectedDayListener();
    }

    private String getDateStr(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setOnSelectedDayListener(final OnSelectedDayListener onSelectedDayListener) {
        this.mMonthAdapter.setOnSelectedDayListener(new OnSelectedDayListener() {
            @Override
            public void onDaySelected(Calendar startDay, Calendar endDay) {
                onSelectedDayListener.onDaySelected(startDay, endDay);
            }
        });
    }
}
