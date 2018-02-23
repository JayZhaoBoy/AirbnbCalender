package com.example.jayzhao.airbnbcalender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.airbnb_calender.util.DateUtil;
import com.example.airbnb_calender.view.AirCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private AirCalendarView mAirCalendarView;
    private String mStartTime, mEndTime;
    private TextView mTextViewStart, mTextViewEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        List<List<String>> allSelDays = (List<List<String>>) intent.getSerializableExtra("allSelDays"); //不可选日期集合

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.DAY_OF_MONTH, 5);
        Date dateStart = calendarStart.getTime();
        Date dateEnd = calendarEnd.getTime();

        mStartTime = DateUtil.dateToString("yyyy-MM-dd", dateStart);
        mEndTime = DateUtil.dateToString("yyyy-MM-dd", dateEnd);

        mTextViewStart = (TextView) findViewById(R.id.startTime);
        mTextViewEnd = (TextView) findViewById(R.id.endTime);
        mTextViewStart.setText(mStartTime);
        mTextViewEnd.setText(mEndTime);

        mAirCalendarView = (AirCalendarView) findViewById(R.id.airCalendarView);
        mAirCalendarView.setHasBeenSelected(allSelDays); //设置不可选日期集合
        mAirCalendarView.setDefaultDateSel_str(mStartTime, mEndTime); //设置默认时间
        mAirCalendarView.setOnSelectedDayListener(new AirCalendarView.OnSelectedDayListener() { //点击日期回调
            @Override
            public void onDaySelected(Calendar startDay, Calendar endDay) {
                Date startSelDate = startDay == null ? null : startDay.getTime();
                Date endSelDate = endDay == null ? null : endDay.getTime();
                mStartTime = startSelDate == null ? "" : DateUtil.dateToString("yyyy-MM-dd", startSelDate);
                mEndTime = endSelDate == null ? "" : DateUtil.dateToString("yyyy-MM-dd", endSelDate);
                mTextViewStart.setText(mStartTime);
                mTextViewEnd.setText(mEndTime);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_clean:
                mAirCalendarView.clearSelect();
                mTextViewStart.setText("");
                mTextViewEnd.setText("");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
