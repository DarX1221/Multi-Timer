package com.example.multitimer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiofx.Virtualizer;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    Boolean alarmSet;
    MediaPlayer mp;
    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    @Override
    public void onReceive(Context context, Intent intent) {
        TimerActivity timerActivity = TimerActivity.instance();
        //mp = MediaPlayer.create(context, alarmUri);





    }

}
