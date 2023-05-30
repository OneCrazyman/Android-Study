package com.example.touchtime10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class MainActivity extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    CalendarClickListener calendarClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCalendarView();
    }


    protected void setCalendarView(){
        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.addDecorator(
                new SundayDecorator()
        );
        calendarClickListener = new CalendarClickListener(this);
        materialCalendarView.setOnDateChangedListener(calendarClickListener);
    }
}