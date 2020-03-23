package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    void stopWatchActivityStart(){
        Intent intent = new Intent(this, StopWatchActivity.class);
        startActivity(intent);
    }
    void timerActivityStart(){
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);

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
