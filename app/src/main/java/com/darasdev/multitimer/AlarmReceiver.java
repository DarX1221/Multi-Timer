package com.darasdev.multitimer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import static com.darasdev.multitimer.App.CHANNEL_1_ID;

public class AlarmReceiver extends BroadcastReceiver {
    Boolean alarm;
    private NotificationManagerCompat notificationManager;
    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    MediaPlayer mediaPlayer;


    @Override
    public void onReceive(Context context, Intent intent) {
        alarm = intent.getBooleanExtra("power", false);
        Intent activityIntent = new Intent(context, TimerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);

        if(true) {
            notificationManager = NotificationManagerCompat.from(context);
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Click me to open timers")
                    //.setContentText("")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .setVibrate(new long[]{1000, 1000, 1000})
                    //.setAutoCancel(true)
                    //.addAction(R.mipmap.ic_launcher_round, "Toast", contentIntent )
                    .build();
            notificationManager.notify(1, notification);


            mediaPlayer = MediaPlayer.create(context, alarmUri);


             try{
                 //Thread.sleep(1500);
                 mediaPlayer.start();
                 Thread.sleep(5000);
             }
             catch (InterruptedException e){}
            mediaPlayer.stop();


        }
    }


}
