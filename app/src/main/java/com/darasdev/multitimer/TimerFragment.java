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
public class TimerFragment extends Fragment
        implements View.OnClickListener{
    boolean running = false;
    String nameTimer = "Name Timer";
    TextView textView;
    TextView timerValue;
    TimerActivity timerActivity;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        textView = view.findViewById(R.id.timer_name);
        Button startButton = (Button) view.findViewById(R.id.start_button_timer);
        startButton.setOnClickListener(this);
        Button stopButton = (Button) view.findViewById(R.id.stop_button_timer);
        stopButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.reset_button_timer);
        resetButton.setOnClickListener(this);
        Button setButton = (Button) view.findViewById(R.id.setting_button_timer);
        setButton.setOnClickListener(this);
        timerValue = (TextView) view.findViewById(R.id.timer_value_text);
        timerValue.setOnClickListener(this);

        textView.setText(nameTimer);

        //Uruchomienie funkcji Timer dla tego(this) fragmentu
        runTimer(view);

        //Odwołanie do aktywności, umożliwia przechwycenie adresu pierwszego fragmentu
        timerActivity = (TimerActivity) getActivity();
        timerActivity.setFragment(this);



        if(timerStringTime == null){
            timerValue.setText("00:00:00");
        }
        else {
            timerValue.setText(timerStringTime);
        }
        setEndTimerInActivity();
        //countDownSeconds = (int)(countDownValueSeconds - (clockSum / 1000));
        //setTimerTextviewMilis(countDownSeconds);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button_timer:
                startChronometer(view);
                break;
            case R.id.stop_button_timer:
                stopChronometer(view);
                break;
            case R.id.reset_button_timer:
                resetChronometer(view);
                break;
            case R.id.setting_button_timer:
                openSettingFragment(view, this);
                break;
            case R.id.timer_value_text:
                openSettingFragment(view, this);
                break;
        }
    }

    String timerStringTime;
    int seconds, countDownSeconds, countDownValueSeconds;
    long clockNow, clockStop, timerEndClock;
    long clockSum = 0L;
    long clockStart = System.currentTimeMillis();
    public void runTimer(final View view) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    clockNow = System.currentTimeMillis();
                    seconds = (int) ((clockNow - clockStart + clockSum) / 1000);
                    countDownSeconds = countDownValueSeconds - seconds;
                    secondsToTimeFormatString(countDownSeconds);
                    timerValue.setText(timerStringTime);
                    if((countDownSeconds <= -1) && countDownSeconds > -2){      // app need time to run MediaPlayer
                        timerActivity.alarmStart();

                    }
                    if(countDownSeconds <= -1){
                        running = false;
                        timerStringTime = secondsToTimeFormatString(0);
                        timerValue.setText(timerStringTime);}
                }
                handler.postDelayed(this, 100);
            }

        });
    }


    void setTimerTextviewSeconds(int secondsBufor){
        timerStringTime = secondsToTimeFormatString(secondsBufor);
        if(timerValue != null) {
            timerValue.setText(timerStringTime);
        }
    }
    void setTimerTextviewMilis(long milis){
        //  Change Vale of time in milis to time format and set on TextView
        seconds = (int) (milis / 1000);
        timerStringTime = secondsToTimeFormatString(seconds);
        timerValue.setText(timerStringTime);
    }


    String secondsToTimeFormatString(int seconds2){
        int seconds = seconds2;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        timerStringTime = String.format("%02d:%02d:%02d", hours, minutes, secs);
        return timerStringTime;
    }

    public void startChronometer(View view) {
        if (!running && countDownValueSeconds > 1) {
            running = true;
            clockStart = System.currentTimeMillis();
            timerEndClock = clockStart + (countDownValueSeconds * 1000);

            timerActivity.setEndTimers(getID(), timerEndClock);
                //timerActivity.alarmStart();

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
        timerStringTime = "00:00:00";
        timerValue.setText(timerStringTime);
    }
    public void setChronometer(){
        int secondsBuf = (int)clockSum / 1000;
        String time = secondsToTimeFormatString(secondsBuf);
        timerValue.setText(time);
    }

    boolean showSettings = true;    //Boolean settings visibility of settings Fragment
    SettingsTimerFragment settingsTimerFragment;
    public void openSettingFragment(View view, TimerFragment timerFragment){
        int idBuffor = timerFragment.getID();

        FragmentTransaction transactionSetTim = timerFragment.getChildFragmentManager().beginTransaction();
        transactionSetTim.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        settingsTimerFragment = new SettingsTimerFragment();
        settingsTimerFragment.setID(idBuffor);
        settingsTimerFragment.setType(false);
        transactionSetTim.replace(R.id.settings_container, settingsTimerFragment);

        if(timerFragment.showSettings){
            transactionSetTim.replace(R.id.settings_container, settingsTimerFragment);
            timerFragment.showSettings = false;}
        else{
            transactionSetTim.remove(settingsTimerFragment);
            timerFragment.showSettings=true; }

        transactionSetTim.commit();
    }


    public void setName(String name){
        nameTimer = name;
        textView.setText(name);}
        //  Setter of countdown timer
        void setTimerValue(int minutes){
        countDownValueSeconds = minutes * 60;
        timerValue.setText(secondsToTimeFormatString(minutes*60));
        }
    void setTimerValueSeconds(int seconds){
        timerValue.setText(secondsToTimeFormatString(seconds));
    }



    public int getCountdownTimerValueSeconds(){
        return countDownValueSeconds;
    }


    // YENBI
    public int getCountdownTimerValue(){
        int minutes = countDownValueSeconds * 60;
        return minutes;
    }
    public int getCountdownTimerValueSec(){
        int secondsBufor = countDownValueSeconds;
        return secondsBufor;
    }



        private int idTim;
        public void setID(int id){   idTim = id;}
        public int getID(){     return idTim; }

    void setEndTimerInActivity(){
        long endTimer = getEndTimerClock();
        timerActivity.setEndTimers(getID(), endTimer);
    }
        long getEndTimerClock() {
            return timerEndClock;
        }

        public TimerActivity getTimerActivity() {
            return timerActivity;
        }

        public void deleteTimer(){
            TimerActivity timerAct = getTimerActivity();
            timerAct.deleteTimer(getID(), this);
        }




}







