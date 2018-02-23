package com.example.airbnb_calender.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airbnb_calender.R;
import com.example.airbnb_calender.model.CalendarDayModel;
import com.example.airbnb_calender.model.CalendarMonthModel;
import com.example.airbnb_calender.model.DateRange;
import com.example.airbnb_calender.util.CalenderHasRoomTypeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jayzhao on 2018/1/11.
 */

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder>
        implements MonthView.OnDayClickListener {
    private List<Calendar> months;
    private Map<Calendar, CalendarMonthModel> monthModelMap;
    private DateRange dateRange;
    private RecyclerView mRecyclerView;
    private AirCalendarView.OnSelectedDayListener onSelectedDayListener;

    public MonthAdapter(List<Calendar> months, final DateRange dateRange,
                        RecyclerView recyclerView) {
        this.months = months;
        this.mRecyclerView = recyclerView;
        this.dateRange = dateRange;
        monthModelMap = new HashMap<>();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                dateRange.clickDay(null, getAllCalendarModel());
                invalidateAllView();
            }
        });
//        clearSelect();
    }

    public void clearSelect() {
        if (CalenderHasRoomTypeHelper.MODEL !=null && CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart){
            CalenderHasRoomTypeHelper.MODEL.isCanNotBeStart = false;
            CalenderHasRoomTypeHelper.MODEL.isHasBeenChose = true;
        }
        CalenderHasRoomTypeHelper.FIRST_SELECTED_BEHIND_START = null;
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                dateRange.setEndDate(null);
                dateRange.setStartDate(null);
                dateRange.clickDay(null, getAllCalendarModel());
                invalidateAllView();
            }
        });
    }

    public void addBefore(List<Calendar> months) {
        ArrayList<Calendar> newList = new ArrayList<>();
        newList.addAll(months);
        newList.addAll(this.months);
        this.months = newList;
    }

    public void addAfter(List<Calendar> months) {
        this.months.addAll(months);
    }

    private CalendarMonthModel getCalendarMonthModel(int position) {
        Calendar date = months.get(position);
        List<List<String>> allDays_sel = dateRange.getHasBeenSelectedDays();
        CalendarMonthModel monthModel = monthModelMap.get(date);
        if (monthModel == null) {
            List<String> days = null;
            if (allDays_sel != null && position < allDays_sel.size()) {
                days = dateRange.getHasBeenSelectedDays().get(position);
            }
            monthModel = new CalendarMonthModel(date, days);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    dateRange.clickDay(null, getAllCalendarModel());
                    invalidateAllView();
                }
            });
            monthModelMap.put(date, monthModel);
        } else {
        }
        return monthModel;
    }

    @Override
    public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aircalendar, parent, false);
        return new MonthHolder(view, this);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public Calendar getItem(int position) {
        return months.get(position);
    }

    private List<CalendarMonthModel> getAllCalendarModel() {
        Collection<CalendarMonthModel> values = monthModelMap.values();
        List<CalendarMonthModel> list = new ArrayList<>();
        list.addAll(values);
        return list;
    }

    @Override
    public void onBindViewHolder(MonthHolder holder, int position) {
        CalendarMonthModel monthModel = getCalendarMonthModel(position);
        holder.monthView.setCalendarMonthModel(monthModel);
    }

    @Override
    public void onDayClick(MonthView monthView, CalendarDayModel day) {
        boolean b = dateRange.clickDay(day, getAllCalendarModel());
        if (b) {
            invalidateAllView();
            if (onSelectedDayListener != null) {
                onSelectedDayListener.onDaySelected(
                        dateRange.getStartDate(), dateRange.getEndDate());
            }
        }
    }

    private void invalidateAllView() {
        int count = mRecyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = mRecyclerView.getChildAt(i);
            v.invalidate();
        }
    }

    public AirCalendarView.OnSelectedDayListener getOnSelectedDayListener() {
        return onSelectedDayListener;
    }

    public void setOnSelectedDayListener(AirCalendarView.OnSelectedDayListener onSelectedDayListener) {
        this.onSelectedDayListener = onSelectedDayListener;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    static class MonthHolder extends RecyclerView.ViewHolder {
        MonthView monthView;

        public MonthHolder(View itemView, MonthView.OnDayClickListener listener) {
            super(itemView);
            monthView = (MonthView) itemView;
            monthView.setDayClickListener(listener);
        }
    }
}