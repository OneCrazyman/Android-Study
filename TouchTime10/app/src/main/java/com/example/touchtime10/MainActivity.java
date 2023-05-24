package com.example.touchtime10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;

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
    }
}