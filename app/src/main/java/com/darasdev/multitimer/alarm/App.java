package com.darasdev.multitimer.alarm;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        
    }

    void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_1_ID, "channel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Description some say");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

    }



    // if work delete all class!!
}
