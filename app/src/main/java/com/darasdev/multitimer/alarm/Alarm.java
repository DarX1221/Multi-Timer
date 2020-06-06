package com.darasdev.multitimer.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.PowerManager;
import android.widget.Toast;

    public class Alarm extends BroadcastReceiver
    {
        PowerManager.WakeLock wl;
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Tag tag = null;
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE, "");
                    //new WakeLock(PowerManager.PARTIAL_WAKE_LOCK, "All");

            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNjfdhotDimScreen");
            wl.acquire();

            // Put here YOUR code.
            Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

            wl.release();
        }

        public void setAlarm(Context context)
        {
            AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Alarm.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
            onReceive(context, i);
        }

        public void cancelAlarm(Context context)
        {
            Intent intent = new Intent(context, Alarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }




