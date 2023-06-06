package com.example.touchtime10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

import com.example.touchtime10.dao.EventDao;
import com.example.touchtime10.entity.Event;
import com.example.touchtime10.util.RoomDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DateDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView list;
    private ArrayList<Event> events;
    private ImageButton addButton;
    private RoomDB database;
    private EventDao eventDao;
    private String date;
    private CustomList adapter;
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
        adapter = new CustomList(DateDetailsActivity.this, events);
        list.setAdapter(adapter);
        addButton.setOnClickListener(this);
    }

    public class CustomList extends ArrayAdapter<Event> {
        private final Activity context;
        private List<Event> events;
//        private OnCheckBoxClickListener onCheckBoxClickListener;

//        public interface OnCheckBoxClickListener {
//            void onCheckBoxClick(int eventId, boolean isChecked);
//        }

        public CustomList(Activity context, List<Event> events) {
            super(context, R.layout.itemlist_date_details, events);
            this.context = context;
            this.events = events;
//            this.onCheckBoxClickListener = listener;
        }

        public void reset(){
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.itemlist_date_details, null, true);
            CheckBox checkBox = rowView.findViewById(R.id.radio_button);
            Button modifyButton = rowView.findViewById(R.id.modify_button);
            Button deleteButton = rowView.findViewById(R.id.delete_button);
            Event event = events.get(position);
            String item = event.getItem();
            checkBox.setText(item);
            checkBox.setChecked(event.isEventCheck());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                        onCheckBoxClickListener.onCheckBoxClick(event.getId(),isChecked);
                        Log.v("print", "check_box"+isChecked);
                        eventDao.updateCheck(event.getId(),isChecked ? 1:0);

                }
            });

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

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DateDetailsActivity.this);

                LayoutInflater inflater = LayoutInflater.from(DateDetailsActivity.this);
                View dialogView = inflater.inflate(R.layout.popup_dialog, null);
                dialogBuilder.setView(dialogView);

                LinearLayout linearLayoutCash = dialogView.findViewById((R.id.linear_cash));
                RadioButton radioAccountWithdrawal = dialogView.findViewById(R.id.radio_account_withdrawal);
                RadioButton radioSchedule = dialogView.findViewById(R.id.radio_schedule);
                Spinner spinnerCashOperation = dialogView.findViewById(R.id.spinner_cash_operation);
                EditText editSchedule = dialogView.findViewById(R.id.edit_schedule);
                EditText editAmount = dialogView.findViewById(R.id.edit_amount);
                Button btnOk = dialogView.findViewById(R.id.btn_ok);

                // Create and show the dialog
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                //금전출납
                radioAccountWithdrawal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            linearLayoutCash.setVisibility(View.VISIBLE);
                            spinnerCashOperation.setVisibility(View.VISIBLE);
                            editAmount.setVisibility(View.VISIBLE);
                            editSchedule.setVisibility(View.GONE);
                        }
                    }
                });

                //일정등록 버튼
                radioSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            linearLayoutCash.setVisibility(View.GONE);
                            spinnerCashOperation.setVisibility(View.GONE);
                            editAmount.setVisibility(View.GONE);
                            editSchedule.setVisibility(View.VISIBLE);

                        }
                    }
                });

                // Handle OK button click
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isAccountWithdrawalSelected = radioAccountWithdrawal.isChecked();
                        boolean isScheduleSelected = radioSchedule.isChecked();
                        String scheduleName = editSchedule.getText().toString().trim();
                        String amount = editAmount.getText().toString().trim();

                        //db에 등록
                        if (isAccountWithdrawalSelected){
                            //계좌 출납입 리스트에 등록
                        }
                        else if(isScheduleSelected){
                            Event event_new = new Event(date, scheduleName);
                            eventDao.insert(event_new);

                            //어댑터 초기화
                            finish();
                            Intent intent = new Intent(DateDetailsActivity.this,DateDetailsActivity.class);
                            intent.putExtra("selectedDate",date);
                            startActivity(intent);

                        }
                        alertDialog.dismiss();
                    }
                });

                //스피너 어댑터
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(DateDetailsActivity.this,
                        android.R.layout.simple_spinner_item, new String[]{"입금", "출금"});
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCashOperation.setAdapter(spinnerAdapter);
                

        }
    }
}
