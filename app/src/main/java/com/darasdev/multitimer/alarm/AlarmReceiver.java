package com.darasdev.multitimer.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.darasdev.multitimer.R;
import com.darasdev.multitimer.timer.TimerActivity;
import static com.darasdev.multitimer.App.CHANNEL_1_ID;



public class AlarmReceiver extends BroadcastReceiver {
    Boolean alarm;
    private NotificationManagerCompat notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        alarm = intent.getBooleanExtra("power", false);

        Intent activityIntent = new Intent(context, TimerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Alarm sound
        Alarm al = Alarm.getInstance();
        al.playalarmSoundForXSeconds(context, 30);


        //Notification
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


