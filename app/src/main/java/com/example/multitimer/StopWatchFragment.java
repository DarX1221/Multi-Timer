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


/**
 * A simple {@link Fragment} subclass.
 */
public class StopWatchFragment extends Fragment implements View.OnClickListener {
    boolean running = false;


    public StopWatchFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);


        Button startButton = (Button) view.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        Button stopButton = (Button) view.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        Button setButton = (Button) view.findViewById(R.id.setting_button);
        setButton.setOnClickListener(this);
        runTimer(view);

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
           /* case R.id.setting_button:
                openSettingFragment(view);
                break;*/
        }
    }


    String stopWatchTime;
    int seconds;
    long clockNow, clockStop, clockSum;
    long clockStart = System.currentTimeMillis();

    public void runTimer(final View view) {


        final TextView stopWatchValue = (TextView) view.findViewById(R.id.timer_text);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (running) {
                    clockNow = System.currentTimeMillis();
                    seconds = (int) ((clockNow - clockStart + clockSum) / 1000);

                    int hours = seconds / 3600;
                    int minutes = (seconds % 3600) / 60;
                    int secs = seconds % 60;
                    stopWatchTime = String.format("%02d:%02d:%02d", hours, minutes, secs);

                    stopWatchValue.setText(stopWatchTime);

                }
                handler.postDelayed(this, 100);
                if (seconds == 0) {
                    stopWatchValue.setText("00:00:00");
                }
            }

        });
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
    }

   /* boolean showSettings = true;
    void openSettingFragment(View view){
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction transactionSet = getFragmentManager().beginTransaction();
        transactionSet.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transactionSet.remove(settingsFragment);

        if(showSettings){
            transactionSet.replace(R.id.fragment_set_conteiner, settingsFragment);
            showSettings = false;}
        else {
            transactionSet.remove(settingsFragment);
            showSettings=true; }
        transactionSet.commit();
    }*/





    String name;
    void setName(){


    }

}

