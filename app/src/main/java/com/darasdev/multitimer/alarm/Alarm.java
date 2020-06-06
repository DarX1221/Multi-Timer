package com.darasdev.multitimer.alarm;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;



public class Alarm {
    public static final Alarm INSTANCCE = new Alarm();

    private Alarm(){}
    public static Alarm getInstance() {
        return INSTANCCE;
    }

    static MediaPlayer mediaPlayer;
    static final Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);


    /**
     * The Alarm sounds until it stops by mediaPlayer.stop()
     * @param context
     */
    public void alarmSound(Context context) {
        mediaPlayer = MediaPlayer.create(context, alarmUri);
        mediaPlayer.start();
    }


    /**
     * The Alarm is turn on for X seconds
     * @param context
     * @param seconds
     */
    public void playalarmSoundForXSeconds(Context context, int seconds) {

            try {
                mediaPlayer = MediaPlayer.create(context, alarmUri);
                mediaPlayer.start();
            }catch(Exception e) {
                e.printStackTrace();
            }

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        mediaPlayer.stop();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, seconds * 1000);
        }



    public static void stopAlarm() {


        try {
            mediaPlayer.stop();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
