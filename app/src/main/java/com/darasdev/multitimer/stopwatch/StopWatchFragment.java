package com.darasdev.multitimer.stopwatch;

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

import com.darasdev.multitimer.R;

import static com.darasdev.multitimer.MainActivity.SENSITIVY_OF_TOUCHSCREEN;


public class StopWatchFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    boolean running = false;
    String nameTimer = "Name Timer";
    TextView textView;
    TextView stopWatchValue, sumTextView;
    StopWatchActivity stopWatchActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create all view's
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
        sumTextView = (TextView) view.findViewById(R.id.sum_timer_text);
        sumTextView.setText(secondsToTime(saveSeconds));
        view.setOnTouchListener(this);

        try {
            initializeFragment();
        } catch (Exception ex) {
            Log.e("Stopwatch Fragment ", "Initialize Error");
        }

        return view;
    }

    private void initializeFragment() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenDPI = metrics.densityDpi;
        touchBufor = SENSITIVY_OF_TOUCHSCREEN * screenDPI;

        stopWatchValue.setOnClickListener(this);
        textView.setText(nameTimer);

        //Run timer in this Fragment
        setTimer(clockSum);
        runTimer();


        // Reference to Activity, it let's take the first Fragment create by XML.
        stopWatchActivity = (StopWatchActivity) getActivity();
        stopWatchActivity.setFragment(this);

        textView.setOnTouchListener(this);
        stopWatchValue.setOnTouchListener(this);
        sumTextView.setOnTouchListener(this);

    }


    // swipe screen(changing activity)
    private float x1, x2, y1, y2, xm, ym;
    float screenDPI;
    float touchBufor;

    boolean shouldClick = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                shouldClick = true;
                break;

            //case MotionEvent.ACTION_BUTTON_PRESS:

            case MotionEvent.ACTION_UP:
                if (shouldClick) {
                    v.performClick();
                }
                x2 = event.getX();
                y2 = event.getY();
                break;
        }
        ;

        if (x1 != 0 && x2 != 0 && showSettings) {

            if (x1 > x2 + touchBufor) {
                stopWatchActivity.openAnotherActivity(true, false);
                //Toast.makeText(getContext(), "Left", Toast.LENGTH_SHORT).show();
            }
            if (x1 + touchBufor < x2) {
                //Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT).show();
                stopWatchActivity.openAnotherActivity(false, true);
            }
        }
        return true;
    }


    // Engine of timer
    String stopWatchTime;
    int seconds;
    long clockNow, clockStop;
    long clockSum;
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
                        stopWatchTime = secondsToTime(seconds);
                        stopWatchValue.setText(stopWatchTime);

                    }
                    handler.postDelayed(this, 100);
                }

            });
        } catch (Exception ex) {
        }
    }

    String secondsToTime(int seconds2) {
        int seconds = seconds2;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        stopWatchTime = String.format("%02d:%02d:%02d", hours, minutes, secs);
        return stopWatchTime;
    }

    // React to buttons ()
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
        saveSeconds += seconds;
        sumTextView.setText(secondsToTime(saveSeconds));
        running = false;
        clockSum = 0;
        seconds = 0;
        stopWatchTime = "00:00:00";
        stopWatchValue.setText(stopWatchTime);
    }

    public void setChronometer() {
        int secondsBuf = (int) clockSum / 1000;
        String time = secondsToTime(secondsBuf);
        stopWatchValue.setText(stopWatchTime);
    }

    int saveSeconds;

    public void setSaveSeconds(int saveSeconds) {
        this.saveSeconds = saveSeconds;
    }

    public void setSaveSecondsTextView(int saveSeconds) {
        this.saveSeconds = saveSeconds;
        sumTextView.setText(secondsToTime(saveSeconds));
    }

    public int getSaveSeconds() {
        return saveSeconds;
    }


    // part responsible for sliding SettingsFragment
    boolean showSettings = true;
    SettingsFragment settingsFragment;

    public void openSettingFragment(View view, StopWatchFragment stopWatchFragment) {
        int idBuffor = stopWatchFragment.getID();

        FragmentTransaction transactionSet = stopWatchFragment.getChildFragmentManager().beginTransaction();
        transactionSet.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        settingsFragment = new SettingsFragment();
        settingsFragment.setID(idBuffor);
        transactionSet.replace(R.id.settings_container, settingsFragment);

        if (stopWatchFragment.showSettings) {
            transactionSet.replace(R.id.settings_container, settingsFragment);
            stopWatchFragment.showSettings = false;
        } else {
            transactionSet.remove(settingsFragment);
            stopWatchFragment.showSettings = true;
        }

        transactionSet.commit();
    }

    void setTimer(long milis) {
        seconds = (int) (milis / 1000);
        stopWatchTime = secondsToTime(seconds);
        stopWatchValue.setText(stopWatchTime);
    }

    public void setName(String name) {
        nameTimer = name;
        if (name == null) {
            name = "Null name timer";
        }
        textView.setText(name);
    }


    // each Fragment has own ID in Activity
    int idSW;

    public void setID(int id) {
        idSW = id;
    }

    public int getID() {
        return idSW;
    }

    public StopWatchActivity getStopWatchActivity() {
        return stopWatchActivity;
    }

    public void deleteStopWatch() {
        StopWatchActivity swAct = getStopWatchActivity();
        swAct.deleteTimer(getID(), this);
    }

}

