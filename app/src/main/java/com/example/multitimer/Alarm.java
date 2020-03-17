package com.example.multitimer;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//import static com.example.multitimer.App.CHANNEL_ID;

public class Alarm extends BroadcastReceiver {
    Boolean alarmSet;
    MediaPlayer mp;
    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    @Override
    public void onReceive(Context context, Intent intent) {
        TimerActivity timerActivity = TimerActivity.instance();
        //mp = MediaPlayer.create(context, alarmUri);
/*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                //.setSmallIcon(R.drawable.);
                .setContentTitle("Alarm")
                .setContentText("Some text trolo")
                .setPriority(NotificationCompat.PRIORITY_HIGH);





*/


    }

}
