package com.darasdev.multitimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.darasdev.multitimer.stopwatch.StopWatchActivity;
import com.darasdev.multitimer.sum.SumStopwatchActivity;
import com.darasdev.multitimer.timer.TimerActivity;

//  Main window of application, its deprecated!!

public class MainActivity extends AppCompatActivity {
    public static final float SENSITIVY_OF_TOUCHSCREEN = 0.4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopWatchActivityStart();
    }

    void stopWatchActivityStart(){
        Intent intent = new Intent(this, StopWatchActivity.class);
        startActivity(intent);
        finish();
    }

    void timerActivityStart(){
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
        finish();
    }

    public MainActivity getMainActivity(){
        return this;
    }

    public void addClickStopwatch(View view) {
        stopWatchActivityStart();
    }

    public void addClickTimer(View view) {
        timerActivityStart();
    }
}

