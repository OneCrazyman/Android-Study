package com.example.touchtime10;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarClickListener implements OnDateSelectedListener {
    private Context context;
    private ArrayList<CalendarDay> dates = new ArrayList<CalendarDay>();

    public CalendarClickListener(Context context) {
        this.context = context;
    }

    //날짜가 선택됐을때 처리
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.v("print", String.valueOf(date.getDate()));
        Intent intent = new Intent(context, DateDetailsActivity.class);

        //인탠트에 선택된 날짜 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        intent.putExtra("selectedDate", String.valueOf

                (sdf.format(date.getDate())));
        context.startActivity(intent);
    }
}
