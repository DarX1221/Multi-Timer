package com.example.multitimer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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

       /* SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction transactionSet = getChildFragmentManager().beginTransaction();
        transactionSet.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transactionSet.replace(R.id.settings_container, settingsFragment);
        transactionSet.commit();*/


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

    boolean showSettings = true;
    boolean create = false;

    public void openSettingFragment(View view){


        SettingsFragment settingsFragment = new SettingsFragment();
        Toast toast = Toast.makeText(getContext(), "settings", Toast.LENGTH_LONG);
        toast.show();
        FragmentTransaction transactionSet = getChildFragmentManager().beginTransaction();
        transactionSet.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transactionSet.replace(R.id.settings_container, settingsFragment);
        //transactionSet.remove(settingsFragment);





        /*SettingsFragment settingsFragment2 = new SettingsFragment();
        Toast toast2 = Toast.makeText(getContext(), "settings2", Toast.LENGTH_LONG);
        toast2.show();
        FragmentTransaction transactionSet2 = getChildFragmentManager().beginTransaction();
        transactionSet2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //transactionSet2.replace(R.id.settings_container, settingsFragment2);
        transactionSet2.remove(settingsFragment2);
        transactionSet2.commit();*/



        if(showSettings){

            Toast toast2 = Toast.makeText(getContext(), "settings if true", Toast.LENGTH_SHORT);
            toast2.show();
            transactionSet.replace(R.id.settings_container, settingsFragment);
            showSettings = false;}
        else{
            Toast toast3 = Toast.makeText(getContext(), "settings if false", Toast.LENGTH_SHORT);
            toast3.show();

            transactionSet.remove(settingsFragment);


            showSettings=true; }
        //transactionSet.addToBackStack(null);
        transactionSet.commit();
        //transactionSet.remove(settingsFragment);
    }







    String name;
    void setName(){


    }

}

