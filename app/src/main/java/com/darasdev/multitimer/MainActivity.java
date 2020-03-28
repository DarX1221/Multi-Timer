package com.darasdev.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerActivityStart();
        //stopWatchActivityStart();

    }

    float x1, x2, y1, y2;
    public boolean onTouchEvent (MotionEvent touchevent){

        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if(x1 < x2){
                    Toast.makeText(this, "StopWatch", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, StopWatchActivity.class);
                    startActivity(intent);
                }
                if(x1 > x2){
                    Toast.makeText(this, "Timer", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return false;
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

