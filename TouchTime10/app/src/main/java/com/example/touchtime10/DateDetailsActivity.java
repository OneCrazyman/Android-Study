package com.example.touchtime10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

public class DateDetailsActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_details);
        Intent dateDetailIntent = getIntent();
        String date = dateDetailIntent.getStringExtra("selectedDate");
        TextView tv = findViewById(R.id.textView);
        tv.setText(date);
    }
}
