package com.example.counterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnDown;
    Button btnUp;
    Button btnInputCheck;
    TextView tvNum;
    Integer count;
    Integer UnitNum;
    EditText etInput;

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
        btnInputCheck = (Button) findViewById(R.id.buttonInput);
        tvNum = (TextView) findViewById(R.id.tvNum);
        etInput = (EditText) findViewById(R.id.etInput);

        count = 0;
        UnitNum = 1;

        tvNum.setText(Integer.toString(count));
        etInput.setText(Integer.toString(UnitNum));
    }

    //익명클래스
    public void EventListner() {
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer tmp = count-UnitNum;
                if (tmp >= 0) {
                    count=tmp;
                    Update();
                }
                else{
                    Toast.makeText(getApplicationContext(), "0미만으로 내려갈수없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count+=UnitNum;
                Update();
            }
        });
        btnInputCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnitNum = Integer.parseInt(String.valueOf(etInput.getText()));
            }
        });

    }

    //업데이트
    public void Update(){
        tvNum.setText(Integer.toString(count));
    }
}