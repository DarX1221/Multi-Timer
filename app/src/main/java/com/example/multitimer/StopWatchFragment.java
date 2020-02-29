package com.example.multitimer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class StopWatchFragment extends Fragment implements View.OnClickListener{
    boolean running = false;
    TextView textView;
    TextView stopWatchValue;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);
        textView = view.findViewById(R.id.stop_watch_name);
        Button startButton = (Button) view.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        Button stopButton = (Button) view.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        Button setButton = (Button) view.findViewById(R.id.setting_button);
        setButton.setOnClickListener(this);
        stopWatchValue = (TextView) view.findViewById(R.id.timer_text);
        runTimer(view);

        if(savedInstanceState != null){
            running = savedInstanceState.getBoolean("runningTimer");
            clockSum = savedInstanceState.getLong("clockSum");
            clockStart = savedInstanceState.getLong("clockStart");
            clockStop = savedInstanceState.getLong("clockStop");
            seconds = savedInstanceState.getInt("seconds");

            stopWatchValue.setText(secondsToTime(seconds));
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                startChronometer(view);
                break;
            case R.id.stop_button:
                stopChronometer(view);
                break;
            case R.id.reset_button:
                resetChronometer(view);
                break;
            case R.id.setting_button:
                openSettingFragment(view);
                break;
        }
    }

    String stopWatchTime;
    int seconds;
    long clockNow, clockStop, clockSum;
    long clockStart = System.currentTimeMillis();

    public void runTimer(final View view) {

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (running) {
                    clockNow = System.currentTimeMillis();
                    seconds = (int) ((clockNow - clockStart + clockSum) / 1000);
                    secondsToTime(seconds);
                    stopWatchValue.setText(stopWatchTime);

                }
                handler.postDelayed(this, 100);
            }

        });
    }



    String secondsToTime(int seconds2){
        int seconds = seconds2;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        stopWatchTime = String.format("%02d:%02d:%02d", hours, minutes, secs);
        return stopWatchTime;
    }



    public void startChronometer(View view) {
        if (!running) {
            running = true;
            clockStart = System.currentTimeMillis();
        }
    }

    public void stopChronometer(View view) {
        if (running) {
            running = false;
            clockStop = System.currentTimeMillis();
            clockSum = clockSum + clockStop - clockStart;
        }
    }

    public void resetChronometer(View view) {
        running = false;
        clockSum = 0;
        seconds = 0;
        stopWatchTime = "00:00:00";
        stopWatchValue.setText(stopWatchTime);
    }

    boolean showSettings = true;

    SettingsFragment settingsFragment;
    public void setSettingFragment(SettingsFragment settingFragment){
        this.settingsFragment = settingFragment;
    }
    public void openSettingFragment(View view){

        FragmentTransaction transactionSet = getChildFragmentManager().beginTransaction();
        transactionSet.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transactionSet.replace(R.id.settings_container, settingsFragment);
        if(showSettings){
            transactionSet.replace(R.id.settings_container, settingsFragment);
            showSettings = false;}
        else{
            transactionSet.remove(settingsFragment);
            showSettings=true; }
        //transactionSet.addToBackStack(null);
        transactionSet.commit();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putBoolean("runningTimer", running);
        savedInstanceState.putLong("clockSum", clockSum);
        savedInstanceState.putLong("clockStart", clockStart);
        savedInstanceState.putLong("clockStop", clockStop);
        savedInstanceState.putString("nameTimer", textView.getText().toString());
        savedInstanceState.putInt("seconds", seconds);

    }



    public void setName(String name){
        textView.setText(name);

    }
}

