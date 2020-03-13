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
import android.widget.Toast;


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
        timerValue = (TextView) view.findViewById(R.id.timer_text2);
        timerValue.setOnClickListener(this);

        textView.setText(nameTimer);

        //Uruchomienie funkcji Timer dla tego(this) fragmentu
        runTimer(view);

        //Odwołanie do aktywności, umożliwia przechwycenie adresu pierwszego fragmentu
        timerActivity = (TimerActivity) getActivity();
        timerActivity.setFragment(this);


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
            case R.id.timer_text2:
                Toast.makeText(getContext(), "Set Timer!Fragment", Toast.LENGTH_SHORT).show();
                //setTimerCountdown();
                break;
        }
    }

    String timerStringTime;
    int seconds, countDownSeconds, countDownValueSeconds;
    long clockNow, clockStop, countDownMilis;
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
                    //countDownSeconds = countDownSeconds - seconds;
                    //countDownMilis = countDownMilis - seconds*1000;
                    countDownSeconds = countDownValueSeconds - seconds;
                    secondsToTime(countDownSeconds);
                    timerValue.setText(timerStringTime);
                }
                handler.postDelayed(this, 100);
            }

        });
    }
    void setTimer(long milis){
        seconds = (int) (milis / 1000);
        timerStringTime = secondsToTime(seconds);
        timerValue.setText(timerStringTime);
    }

    String secondsToTime(int seconds2){
        int seconds = seconds2;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        timerStringTime = String.format("%02d:%02d:%02d", hours, minutes, secs);
        return timerStringTime;
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
        timerStringTime = "00:00:00";
        timerValue.setText(timerStringTime);
    }
    public void setChronometer(){
        int secondsBuf = (int)clockSum / 1000;
        String time = secondsToTime(secondsBuf);
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

        void setTimerValue(int minutes){
        countDownValueSeconds = minutes * 60;
        timerValue.setText(secondsToTime(minutes*60));
        }

        int idSW;
        public void setID(int id){   idSW = id;}
        public int getID(){     return idSW; }

        public TimerActivity getTimerActivity() {
            return timerActivity;
        }

        public void deleteTimer(){
            TimerActivity timerAct = getTimerActivity();
            timerAct.deleteTimer(getID(), this);
        }



}







