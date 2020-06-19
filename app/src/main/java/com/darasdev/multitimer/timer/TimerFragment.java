package com.darasdev.multitimer.timer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import static com.darasdev.multitimer.MainActivity.SENSITIVY_OF_TOUCHSCREEN;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.alarm.Alarm;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment
        implements View.OnClickListener, View.OnTouchListener{
    boolean running = false;
    String nameTimer = "Name Timer";
    TextView textView;
    TextView timerValue;
    TimerActivity timerActivity;


    private float mLastScaleFactor = 0;
    private float mTouchY;


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
        view.setOnTouchListener(this);
        setButton.setOnClickListener(this);
        timerValue = (TextView) view.findViewById(R.id.timer_value_text);

        try {
            initializeFragment();
        }
        catch (Exception ex){
            Log.e("Timer Fragment ", "Initialize Error");
        }

        return view;
    }


    private void initializeFragment() {

        // sensitivy of touchscreen
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenDPI = metrics.densityDpi;
        touchBufor = SENSITIVY_OF_TOUCHSCREEN * screenDPI;

        timerValue.setOnClickListener(this);

        textView.setText(nameTimer);

        //Run timer in this Fragment
        runTimer();

        //  First Fragment is create by .XML layout, setFragment() let to catch this Fragment
        timerActivity = (TimerActivity) getActivity();
        timerActivity.setFragment(this);

        // set timer value, if value is null set 0
        if(timerStringTime == null){
            timerValue.setText("00:00:00");
        }
        else {
            timerValue.setText(timerStringTime);
        }
        setEndTimerInActivity();

        textView.setOnTouchListener(this);
        timerValue.setOnTouchListener(this);
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
        }
    }


    //  engine of timer
    String timerStringTime;
    int seconds, countDownSeconds, countDownValueSeconds;
    long clockNow, clockStop, timerEndClock;
    long clockSum = 0L;
    long clockStart = System.currentTimeMillis();
    public void runTimer() {
        try {
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

                        if (countDownSeconds <= 0) {
                            running = false;

                            timerStringTime = secondsToTimeFormatString(0);
                            timerValue.setText(timerStringTime);

                        }
                    }
                    handler.postDelayed(this, 100);         // delayMillis set refresh rate of timer
                }

            });
        }
        catch (Exception ex){}
    }


    /**
     * Convert seconds to String format 00:00:00
     * @param seconds2
     * @return
     */
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
            timerActivity.setLowestEndTimer();

            timerActivity.setAlalrm(timerEndClock);
        }
    }
    public void stopChronometer(View view) {
        if (running) {
            running = false;
            clockStop = System.currentTimeMillis();
            clockSum = clockSum + clockStop - clockStart;

            try {
                timerEndClock = Long.MAX_VALUE;
                timerActivity.endTimersList.set(getID(), Long.MAX_VALUE);

            }
            catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Timer Fragment ", "Stop chr");
            }


        }
        Alarm.stopAlarm();  // clicking pause just turn off alarm
    }
    public void resetChronometer(View view) {
        running = false;
        clockSum = 0;
        seconds = 0;
        timerStringTime = "00:00:00";
        timerValue.setText(timerStringTime);

        try {
            timerEndClock = Long.MAX_VALUE;
            timerActivity.setEndTimers(getID(), Long.MAX_VALUE);
            Alarm.stopAlarm();  // clicking reset just turn off alarm

        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Timer Fragment ", "Reset Chronometer Eroor");
        }
    }


    boolean showSettings = true;    //Boolean set visibility of settings Fragment
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



    //  Getters & Setters
    private int idTim;
    public void setID(int id){   idTim = id;}
    public int getID(){     return idTim; }

    public void setChronometer(){
        int secondsBuf = (int)clockSum / 1000;
        String time = secondsToTimeFormatString(secondsBuf);
        timerValue.setText(time);
    }

   public void setTimerSeconds(int secondsBufor){
        countDownValueSeconds =  secondsBufor;
        timerStringTime = secondsToTimeFormatString(secondsBufor);
        if(timerValue != null) {
            timerValue.setText(timerStringTime);
        }
    }
    void setTimerMilis(long milis){
        seconds = (int) (milis * 1000);
        countDownValueSeconds =  seconds;
        timerStringTime = secondsToTimeFormatString(seconds);
        timerValue.setText(timerStringTime);
    }

    public void setName(String name){
        nameTimer = name;
        textView.setText(name);
    }

    public int getCountdownTimerValueSeconds(){
        return countDownValueSeconds;
    }

    public void setEndTimerInActivity(){
        long endTimer = getEndTimerClock();
        timerActivity.setEndTimers(getID(), endTimer);
    }

    public long getEndTimerClock() {
            return timerEndClock;
        }

        public TimerActivity getTimerActivity() {
            return timerActivity;
        }


        public void deleteTimer(){
            TimerActivity timerAct = getTimerActivity();
            timerAct.deleteTimer(getID(), this);
        }


    // swipe screen(changing activity)
    float x1, x2, y1, y2;
    float screenDPI;
    float touchBufor;
    boolean shouldClick = true;

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                if(shouldClick){
                    v.performClick();
                }
                x2 = event.getX();
                y2 = event.getY();
                break;

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                shouldClick = true;
                break;

            //case MotionEvent.ACTION_BUTTON_PRESS:
        }

        if (x1 != 0 && x2 != 0 && showSettings) {

            if (x1 > x2 + touchBufor) {
                timerActivity.openAnotherActivity(true, false);
                //Toast.makeText(getContext(), "Left", Toast.LENGTH_SHORT).show();
            }
            if (x1 + touchBufor < x2) {
                //Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT).show();
                timerActivity.openAnotherActivity(false, true);
            }
        }

        return true;
    }

}







