package com.example.touchtime10.util;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.touchtime10.R;

public class NotificationReceiver {
    // Channel에 대한 id 생성 : Channel을 구부하기 위한 ID 이다.
    private static final String CHANNEL_ID = "001";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private NotificationCompat.Builder notifyBuilder;

    public void NotificationReceiver(Context context, Intent intent) {
        // 알림을 수신하면 수행할 작업을 여기에 추가하세요
        // 예: 알림 표시, 사운드 재생 등
//        Toast.makeText(context, "알림이 수신되었습니다.", Toast.LENGTH_SHORT).show();

        Log.v("print", "알림 리시브 실행..");
        String scheduleName = intent.getStringExtra("scheduleName");
        mNotificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT
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

        notifyBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("TouchTime")
                .setContentText(scheduleName)
                .setSmallIcon(R.drawable.ic_launcher_add_foreground)
                .setAutoCancel(true); //notification 탭하면 알림을 없앤다.

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
}

