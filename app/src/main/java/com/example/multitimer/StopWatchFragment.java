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


public class StopWatchFragment extends Fragment implements View.OnClickListener{
    boolean running = false;
    String nameTimer = "Name Timer";
    TextView textView;
    TextView stopWatchValue;
    StopWatchActivity stopWatchActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Utworzenie wszystkich potrzebnych View
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
        textView.setText(nameTimer);

        //Uruchomienie funkcji Timer dla tego(this) fragmentu
        setTimer(clockSum);
        runTimer(view);

        //Odwołanie do aktywności, umożliwia przechwycenie adresu pierwszego fragmentu
        stopWatchActivity = (StopWatchActivity) getActivity();
        stopWatchActivity.setFragment(this);

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
                openSettingFragment(view, this);
                break;
        }
    }

    String stopWatchTime;
    int seconds;
    long clockNow, clockStop;
    long clockSum;
    long clockStart = System.currentTimeMillis();
    public void runTimer(final View view) {

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (running) {
                    clockNow = System.currentTimeMillis();
                    seconds = (int) ((clockNow - clockStart + clockSum) / 1000);
                    stopWatchTime = secondsToTime(seconds);
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
    public void setChronometer(){
        int secondsBuf = (int)clockSum / 1000;
        String time = secondsToTime(secondsBuf);
        stopWatchValue.setText(stopWatchTime);
    }

    boolean showSettings = true;
    SettingsFragment settingsFragment;
    public void openSettingFragment(View view, StopWatchFragment stopWatchFragment){
        int idBuffor = stopWatchFragment.getID();

        FragmentTransaction transactionSet = stopWatchFragment.getChildFragmentManager().beginTransaction();
        transactionSet.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        settingsFragment = new SettingsFragment();
        settingsFragment.setID(idBuffor);
        settingsFragment.setType(true);
        transactionSet.replace(R.id.settings_container, settingsFragment);

        if(stopWatchFragment.showSettings){
            transactionSet.replace(R.id.settings_container, settingsFragment);
            stopWatchFragment.showSettings = false;}
        else{
            transactionSet.remove(settingsFragment);
            stopWatchFragment.showSettings=true; }

        transactionSet.commit();
    }

    void setTimer(long milis){
        seconds = (int) (milis / 1000);
        stopWatchTime = secondsToTime(seconds);
        stopWatchValue.setText(stopWatchTime);
    }

    public void setName(String name){
        nameTimer = name;
        textView.setText(name);
        StopWatchActivity sAct = getStopWatchActivity();
        sAct.saveData();}

    int idSW;
    public void setID(int id){   idSW = id;}
    public int getID(){     return idSW; }

    public StopWatchActivity getStopWatchActivity() {
        return stopWatchActivity;
    }

    public void deleteStopWatch(){
        StopWatchActivity swAct = getStopWatchActivity();
        swAct.deleteTimer(getID(), this);

    }

}

