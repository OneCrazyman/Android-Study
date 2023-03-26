package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected Integer randNum;
    protected Integer countNum;
//    TextView textLife = (TextView) findViewById(R.id.textView7);
    public TextView textLife, text1;
    public EditText edit1;
    public Button button1, button2;
    public String life = "❤❤❤❤❤❤❤❤❤❤";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLife = (TextView) findViewById(R.id.textView7);
        text1 = (TextView) findViewById(R.id.textView1);
        edit1 = (EditText) findViewById(R.id.EditText1);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        countNum = 0;
        randNum = randomNumber();

        textLife.setText(life);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer num = Integer.parseInt(edit1.getText().toString());
                countNum++;
                //
                if (num > randNum){
                    text1.setText("!!DOWN!!");
                    lessLife();
                    ifDeath();
                }
                else if (num < randNum){
                    text1.setText("!!UP!!");
                    lessLife();
                    ifDeath();
                }
                else{
                    text1.setText("정답입니다!");
                    String msg = "축하합니다. "+countNum+"번 만에 성공하셨습니다";
                    countNum = 0;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLife.setText(life);
                randNum = randomNumber();
                button1.setEnabled(true);
            }
        });
    }

    protected Integer randomNumber(){
        //랜덤난수생성
        Random random = new Random();
        random.setSeed(System.currentTimeMillis()); // 시드값 설정
        return random.nextInt(100);
    }

    protected void lessLife(){
        String tmp = textLife.getText().toString();
        tmp = tmp.substring(0,tmp.length()-1);
        textLife.setText(tmp);
    }

    protected void ifDeath(){
        //죽음
        if (textLife.getText().toString().equals("")){
            button1.setEnabled(false);
            String msg ="죽었어요";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }
}