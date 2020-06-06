package com.darasdev.multitimer.alarm2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.alarm.Alarm;
import com.darasdev.multitimer.timer.TimerActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.darasdev.multitimer.App.CHANNEL_1_ID;

public class MyReceiver extends BroadcastReceiver {
    private Context context;
    private Intent intent;

    public MyReceiver(){ }

    public MyReceiver(Intent intent, Context context){
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Alarm
        //Alarm2 al = Alarm2.getInstance();
        //al.playalarmSoundForXSeconds(context, 30);

        RingtonePlayerService alarm = new RingtonePlayerService();
        alarm.setContext(context);
        alarm.onStartCommand(intent, Service.START_FLAG_REDELIVERY, 1);
        //alarm.playPauseAlarm();

        // Notification
        NotificationManagerCompat notificationManager;
        Intent activityIntent = new Intent(context, TimerActivity.class);
        notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Click reset or pause to turn off alarm")
                .setContentText("Click me to open timers")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(PendingIntent.getActivity(context, 0, activityIntent, 0))
                .setVibrate(new long[]{1000, 1000, 1000})
                .build();
        notificationManager.notify(1, notification);
    }


}

