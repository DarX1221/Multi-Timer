package com.darasdev.multitimer.alarm2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.darasdev.multitimer.alarm.Alarm;
import com.darasdev.multitimer.timer.AlarmReceiver;


// didnt use
public final class RingtonePlayerService extends Service {
    boolean isPlaying = false;
    private static RingtonePlayerService INSTANCE;
    Context context;
    public void setContext(Context context) {
        this.context = context;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static MediaPlayer mediaPlayer;
    Uri alarmUri;
    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        Log.i("Local Service", "Recived start id" + startId + ":" + intent);
        Log.e("RINGTONE!!!!", "  " + this);

        alarm.onReceive(context, intent);

        alarmUri = alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mediaPlayer = MediaPlayer.create(context, alarmUri);
        mediaPlayer.start();

        //playPauseAlarm();


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "ondestroy", Toast.LENGTH_SHORT).show();
    }

    void playPauseAlarm() {

        alarmUri = alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mediaPlayer = MediaPlayer.create(this, alarmUri);

            mediaPlayer.start();





    }


}
