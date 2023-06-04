package com.example.touchtime10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

import com.example.touchtime10.dao.EventDao;
import com.example.touchtime10.entity.Event;
import com.example.touchtime10.util.RoomDB;

import java.util.ArrayList;
import java.util.List;

public class DateDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    ListView list;
    ArrayList<Event> events;
    ImageButton addButton;
    RoomDB database;
    EventDao eventDao;
    String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_details);
        setIntialize();






    }

    public void setIntialize(){
        //db연결
        database= RoomDB.getInstance(this);
        eventDao = database.eventDao();
        //오늘 날짜 데이터 갖고옴
        Intent dateDetailIntent = getIntent();
        date = dateDetailIntent.getStringExtra("selectedDate");

        //db에서 오늘날짜 event 데이터를 가져와 arraylist에 반환
        events = getEventList(date);

        //temp
        TextView tv = findViewById(R.id.textView);
        tv.setText(date);

        list = (ListView)findViewById(R.id.list);
        addButton = findViewById(R.id.addButton);

        //create customlist adapter
        CustomList adapter = new CustomList(DateDetailsActivity.this);
        list.setAdapter(adapter);


        addButton.setOnClickListener(this);

    }

    public class CustomList extends ArrayAdapter<Event> {
        private final Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.itemlist_date_details, events);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.itemlist_date_details, null, true);
            RadioButton checkButton = rowView.findViewById(R.id.radio_button);
            Button modifyButton = rowView.findViewById(R.id.modify_button);
            Button deleteButton = rowView.findViewById(R.id.delete_button);
            String item = events.get(position).getItem();
            checkButton.setText(item);

            return rowView;
        }
    }

    public ArrayList<Event> getEventList(String date){
//        ArrayList<Event> eventArraylist = new ArrayList<Event>();
        ArrayList<Event> list = (ArrayList<Event>) eventDao.dateToGetEvents(date);
        return list;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.addButton:
                //db삽입
                Event event = new Event("23-06-01", "공부하기");
//                Log.v("print", "its me");
//                EventRoomDatabase db = EventRoomDatabase.getInstance(this);
//                Log.v("print", "its me");
//                EventDao eventDao = db.eventDao();
                eventDao.insert(event);
                Log.v("print", String.valueOf(events));
//                Log.v("print", "its me");
        }
    }
}
