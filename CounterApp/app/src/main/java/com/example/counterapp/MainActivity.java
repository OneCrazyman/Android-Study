package com.example.counterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnDown;
    Button btnUp;
    TextView tvNum;
    Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.IntializeView();
        this.EventListner();
    }

    public void IntializeView() {
        btnDown = (Button) findViewById(R.id.buttonDown);
        btnUp = (Button) findViewById(R.id.buttonUp);
        tvNum = (TextView) findViewById(R.id.tvNum);
        count = 0;
    }

    //익명클래스
    public void EventListner() {
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
            }
        });
        Update();
    }

    //업데이트
    public void Update(){
        tvNum.setText(count);
    }
}