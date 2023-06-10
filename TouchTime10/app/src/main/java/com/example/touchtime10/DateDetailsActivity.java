package com.example.touchtime10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.core.app.NotificationCompat;

import org.jetbrains.annotations.Nullable;

import com.example.touchtime10.dao.CashDao;
import com.example.touchtime10.dao.EventDao;
import com.example.touchtime10.entity.Cash;
import com.example.touchtime10.entity.Event;
import com.example.touchtime10.util.RoomDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView list, list_cash;
    private ArrayList<Event> events;
    private ArrayList<Cash> cashes;
    private ImageButton addButton;
    private RoomDB database;
    private EventDao eventDao;
    private CashDao cashDao;
    private String date;
    private CustomList adapter;
    private CustomList_cash adapter_cash;

    // Channel에 대한 id 생성 : Channel을 구부하기 위한 ID 이다.
    private static final String CHANNEL_ID = "001";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private NotificationCompat.Builder notifyBuilder;

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
        cashDao = database.cashDao();
        //오늘 날짜 데이터 갖고옴
        Intent dateDetailIntent = getIntent();
        date = dateDetailIntent.getStringExtra("selectedDate");
        Log.v("print", "test");
        //db에서 오늘날짜 event 데이터를 가져와 arraylist에 반환
        events = getEventList(date);
        cashes = getCashList(date);
        //temp
        TextView tv = findViewById(R.id.textView);
        tv.setText(date);
        //
        Log.v("print", "test");
        list = (ListView)findViewById(R.id.list);
        list_cash = (ListView)findViewById(R.id.list_cash);
        addButton = findViewById(R.id.addButton);

        //create customlist adapter
        adapter = new CustomList(DateDetailsActivity.this, events);
        list.setAdapter(adapter);
        adapter_cash = new CustomList_cash(DateDetailsActivity.this, cashes);
        list_cash.setAdapter(adapter_cash);
        addButton.setOnClickListener(this);
    }

    public void resetAcitvity(){
        finish();
        Intent intent = new Intent(DateDetailsActivity.this,DateDetailsActivity.class);
        intent.putExtra("selectedDate",date);
        startActivity(intent);
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
                        eventDao.updateCheck(event.getId(),isChecked ? 1:0);
                }
            });
            
            //수정버튼
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DateDetailsActivity.this);

                    LayoutInflater inflater = LayoutInflater.from(DateDetailsActivity.this);
                    View dialogView = inflater.inflate(R.layout.popup_modify_button, null);
                    dialogBuilder.setView(dialogView);

                    EditText editSchedule = dialogView.findViewById(R.id.edit_schedule_two);
                    Button btnOk = dialogView.findViewById(R.id.btn_ok_two);

                    // Create and show the dialog
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String scheduleName = editSchedule.getText().toString().trim();
                            eventDao.updateItem(event.getId(),scheduleName);
                            scheduleNotification(DateDetailsActivity.this ,scheduleName, date);

                            alertDialog.dismiss();

                            finish();
                            Intent intent = new Intent(DateDetailsActivity.this,DateDetailsActivity.class);
                            intent.putExtra("selectedDate",date);
                            startActivity(intent);
                            }
                    });
                }
            });
            
            //삭제버튼
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventDao.delete(event.getId());
                    resetAcitvity();
                }
            });
            return rowView;
        }
    }

    public class CustomList_cash extends ArrayAdapter<Cash> {
        private Activity context;
        private List<Cash> cashes;

        public CustomList_cash(Activity context, List<Cash> cashes) {
            super(context, R.layout.cashlist_date_details, cashes);
            this.context = context;
            this.cashes = cashes;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.cashlist_date_details, null, true);
            Button modifyButton = rowView.findViewById(R.id.modify_button);
            Button deleteButton = rowView.findViewById(R.id.delete_button);
            Cash cash = cashes.get(position);
            String item = cash.getItem();

            //수정버튼
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DateDetailsActivity.this);

                    LayoutInflater inflater = LayoutInflater.from(DateDetailsActivity.this);
                    View dialogView = inflater.inflate(R.layout.popup_modify_button, null);
                    dialogBuilder.setView(dialogView);

                    EditText editCash = dialogView.findViewById(R.id.edit_schedule_two);
                    Button btnOk = dialogView.findViewById(R.id.btn_ok_two);

                    editCash.setText("바꿀 금액을 입력해주세요");

                    // Create and show the dialog
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String cashName = editCash.getText().toString().trim();
                            cashDao.updateItem(cash.getId(),cashName);
//                            scheduleNotification(DateDetailsActivity.this ,scheduleName, date);

                            alertDialog.dismiss();

                            resetAcitvity();
                        }
                    });
                }
            });

            //삭제버튼
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventDao.delete(cash.getId());
                    resetAcitvity();
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

    public ArrayList<Cash> getCashList(String date){
        ArrayList<Cash> list = (ArrayList<Cash>) cashDao.dateToGetCashes(date);
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
                //스피너 어댑터
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(DateDetailsActivity.this,
                        android.R.layout.simple_spinner_item, new String[]{"입금", "출금"});
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCashOperation.setAdapter(spinnerAdapter);

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

                        //계좌 출납입 리스트에 등록
                        if (isAccountWithdrawalSelected){
                            ArrayList<String> cashwayList = new ArrayList<String>(Arrays.asList("입금", "출금"));
                            Log.v("print", String.valueOf(cashwayList.get(1)));
                            int cashway = cashwayList.indexOf(spinnerCashOperation.getSelectedItem().toString());

                            Cash cash_new = new Cash(date,amount,cashway);
                            cashDao.insert(cash_new);

                            resetAcitvity();
                        }

                        //일정등록
                        else if(isScheduleSelected){
                            Event event_new = new Event(date, scheduleName);
                            eventDao.insert(event_new);
                            scheduleNotification(DateDetailsActivity.this ,scheduleName, date);

                            resetAcitvity();
                        }
                        alertDialog.dismiss();
                    }
                });


        }
    }

    private void scheduleNotification(Context context, String scheduleName,String date) {
        // 알림을 설정할 시간을 지정합니다 (예: 현재 시간으로부터 10초 후)
        String day = getDday(date);
        if(day==null){
            return;
        }
//        long delay = Integer.parseInt(day) * 1000; // 10초 (밀리초 단위)
        long delay = 1 * 1000; // 10초 (밀리초 단위)
        Log.v("print", day);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(android.os.Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Test Notification",mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TouchTime")
                .setContentText(scheduleName)
                .setSmallIcon(R.drawable.ic_launcher_add_foreground)
                .setAutoCancel(true); //notification 탭하면 알림을 없앤다.

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());

//         Create a calendar instance and set it to the next midnight
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.DAY_OF_YEAR, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        Log.v("print", String.valueOf(calendar));
//        // Create an intent for your receiver or service that will handle the execution
//        Intent intent = new Intent(this, NotificationReceiver.class);
//        intent.putExtra("scheduleName", scheduleName);
//        Log.v("print", String.valueOf(calendar));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT);
//        Log.v("print", String.valueOf(calendar));
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        Log.v("print", String.valueOf(calendar));
//        // Set the alarm to trigger at midnight
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    //dday 계산 메소드
    private String getDday(String date) {
        //
        String formattedDate1 = "20" + date.replace("-", "-");

        Date currentDate = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedCurrentDate = dateFormat.format(currentDate);

        try {
            Date d1 = dateFormat.parse(formattedDate1);
            Date current = dateFormat.parse(formattedCurrentDate);

            long difference1 = (d1.getTime() - current.getTime()) / (24 * 60 * 60 * 1000);

            String dDay1 = (difference1 >= 0) ? ""+difference1 : null;
            return dDay1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
