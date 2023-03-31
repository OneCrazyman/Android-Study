package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Button startButton, pauseButton, resetButton, checkButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds; // 10 minutes
    private NumberPicker npkerMin, npkerSec;
    private RatingBar ratingBar;
    private boolean TimerRunning;
    //    private TimePicker timerTimePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntializeView();
        EventListner();
    }
    //초기화
    public void IntializeView(){
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        startButton = (Button) findViewById(R.id.startButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        checkButton = (Button) findViewById(R.id.checkButton);
        npkerMin = (NumberPicker) findViewById(R.id.npkerMin);
        npkerSec = (NumberPicker) findViewById(R.id.npkerSec);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        npkerMin.setMaxValue(99); //최대
        npkerMin.setMinValue(0); //최소
        npkerMin.setValue(5);// 초기

        npkerMin.setOnLongPressUpdateInterval(100); //길게 눌렀을 때 반응
        npkerMin.setWrapSelectorWheel(true); //최대최소 임계에서 멈출지 넘어갈지

        npkerSec.setMaxValue(99); //최대
        npkerSec.setMinValue(0); //최소
        npkerSec.setValue(5);// 초기

        npkerSec.setOnLongPressUpdateInterval(100); //길게 눌렀을 때 반응
        npkerSec.setWrapSelectorWheel(true); //최대최소 임계에서 멈출지 넘어갈지

        TimerRunning = false;
    }
    //이벤트리스너
    private void EventListner(){
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), "안녕", Toast.LENGTH_SHORT).show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    //method
    private void startTimer() {
        if (!TimerRunning){ //초기 타이머 설정 reset누른상태에서만 다시 실행됨
            timeLeftInMilliseconds = getNumberNpker();
            TimerRunning = true;
        }
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                // Do something when the timer finishes
            }
        }.start();

        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        resetButton.setEnabled(false);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resetButton.setEnabled(true);
    }

    private void resetTimer() {
        timeLeftInMilliseconds = getNumberNpker();
        updateTimer();
        TimerRunning = false;
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resetButton.setEnabled(false);
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        timerTextView.setText(timeLeftText);
    }

    private int getNumberNpker(){   return (npkerMin.getValue()*60 + npkerSec.getValue())*1000;    }
}
