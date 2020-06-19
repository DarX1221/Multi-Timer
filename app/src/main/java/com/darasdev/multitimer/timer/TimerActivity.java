package com.darasdev.multitimer.timer;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.alarm.Alarm;

import com.darasdev.multitimer.alarm.AlarmReceiver;
import com.darasdev.multitimer.stopwatch.StopWatchActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.darasdev.multitimer.MainActivity.SENSITIVY_OF_TOUCHSCREEN;


public class TimerActivity extends AppCompatActivity
        implements SettingsTimerFragment.FragmentNameListenerTimer {


    private long firstAlarmClock = Long.MAX_VALUE;
    private int firstAlarmClockID;
    private NotificationManagerCompat notificationManager;
    ArrayList<TimerFragment> listOfTim = new ArrayList();
    int lengthOfListTim = listOfTim.size();
    private static TimerActivity INSTANCE;
    int amountOfTimers;
    TextView TVx1;
    TextView TVx2;
    TextView TVy1;
    TextView TVy2;
    TextView TVxm;
    TextView TVym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenDPI = metrics.densityDpi;
        touchBufor = SENSITIVY_OF_TOUCHSCREEN * screenDPI;

        loadData();

        Alarm.stopAlarm();  // clicking reset just turn off alarm
        setNextAlarm();

    }

    // opening TimerActivity stops Timer
    void stopAlarm(){
        Alarm.stopAlarm();
    }


    TimerFragment timerFragment;

    public TimerFragment addTimer(String name, Boolean running, long clockStart, long clockSum, int seconds) {
        timerFragment = new TimerFragment();
        FragmentTransaction fragmentTransactionTim1 = getSupportFragmentManager().beginTransaction();
        listOfTim.add(timerFragment);
        lengthOfListTim = listOfTim.size();
        fragmentTransactionTim1.add(R.id.timer_fragment, listOfTim.get(lengthOfListTim - 1));
        fragmentTransactionTim1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransactionTim1.commit();

        //Setter's
        int ID = lengthOfListTim - 1;
        timerFragment.setID(ID);
        timerFragment.nameTimer = name;
        timerFragment.running = running;
        timerFragment.countDownValueSeconds = seconds;
        timerFragment.clockStart = clockStart;
        timerFragment.clockSum = clockSum;
        int secondsValue = (int) (seconds - (clockSum / 1000));
        timerFragment.setTimerSeconds(secondsValue);
        if(running) {
            timerFragment.timerEndClock = clockStart + clockSum + secondsValue * 1000;
            endTimersList.add(ID, timerFragment.timerEndClock);
        }
        else {
            endTimersList.add(ID, Long.MAX_VALUE);
        }

        return timerFragment;
    }


    //FloatingButton add Fragment
    public void addClick(View view) {
        timerFragment = new TimerFragment();
        listOfTim.add(timerFragment);
        lengthOfListTim = listOfTim.size();
        timerFragment.setID(lengthOfListTim - 1);

        FragmentTransaction fragmentTransactionTim2 = getSupportFragmentManager().beginTransaction();
        fragmentTransactionTim2.add(R.id.timer_fragment, listOfTim.get(lengthOfListTim - 1));
        fragmentTransactionTim2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransactionTim2.commit();
    }


    public void deleteTimer(int id, TimerFragment timerFragment) {
        FragmentTransaction fragmentTransactionTim3 = getSupportFragmentManager().beginTransaction();
        TimerFragment timF;
        if ((listOfTim.size() > 1)) {
            if (id == 0) {  // Delete (set zero's) first Fragment
                int size = listOfTim.size();
                for (int i = 0; i < size - 1; i++) {
                    setTimerFragment(i + 1, i);
                    listOfTim.get(i).setChronometer();
                }
                timF = listOfTim.get(size - 1);
                timF.running = false;
                fragmentTransactionTim3.remove(timF);
                listOfTim.remove(timF);

            } else {  // Delete rest Fragment's (id!=0)
                fragmentTransactionTim3.remove(timerFragment);

                timF = listOfTim.get(id);
                timF.running = false;
                listOfTim.remove(timerFragment);
                lengthOfListTim = listOfTim.size();


                for (int i = 0; i < lengthOfListTim; i++) { //change ID of Fragment's
                    timF = listOfTim.get(i);
                    timF.setID(i);
                }
            }
            fragmentTransactionTim3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransactionTim3.commit();
        }
    }


    //  It is using by SettingTimerFragment and set name and timer value
    String nameTimer;
    @Override
    public void onInputNameSent(String name, String minutesStr, int id) {

        if (minutesStr != null) {                             // Set Timer Value (minutes)
            int minutes = 0;
            TimerFragment timerFragmentBufor = listOfTim.get(id);
            try {
                minutes = Integer.parseInt(minutesStr);
            } catch (Exception ex) {
                Toast.makeText(this, "Only numbers!", Toast.LENGTH_SHORT).show();
            }
            timerFragmentBufor.setTimerSeconds(minutes * 60);
        }

        if (name != null) {                               //  Set name
            nameTimer = name;
            TimerFragment timerFragmentBuffor = listOfTim.get(id);
            timerFragmentBuffor.setName(name);
        }
    }


    //  First Fragment is create by .XML layout, setFragment() let to catch this Fragment
    Boolean firstFragment = false;
    public void setFragment(TimerFragment timerFragment) {
        if (!firstFragment) {
            if (listOfTim.size() <= 1) {
                listOfTim.add(0, new TimerFragment());
                listOfTim.set(0, timerFragment);
            }
        }
        firstFragment = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        INSTANCE = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }


    void saveData() {
        amountOfTimers = listOfTim.size();
        ArrayList<String> listOfNamesTim = new ArrayList<>();
        ArrayList<Boolean> listOfBooleansTim = new ArrayList<>();
        ArrayList<Integer> countDownSecondsValue = new ArrayList<>();
        long[] clockSumTabTim = new long[amountOfTimers];
        long[] clockStartTabTim = new long[amountOfTimers];

        TimerFragment tim;
        for (int i = 0; i < amountOfTimers; i++) {
            tim = listOfTim.get(i);

            String nameT = tim.nameTimer;
            if (nameT == null) {
                nameT = "Name timer:";
            }
            listOfNamesTim.add(nameT);
            listOfBooleansTim.add(tim.running);
            countDownSecondsValue.add(tim.getCountdownTimerValueSeconds());
            clockStartTabTim[i] = tim.clockStart;
            clockSumTabTim[i] = tim.clockSum;
        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("amount_timers", amountOfTimers);

        Gson gson = new Gson();
        String json = gson.toJson(listOfNamesTim);
        editor.putString("key_names", json);

        json = gson.toJson(listOfBooleansTim);
        editor.putString("key_bool", json);

        json = gson.toJson(countDownSecondsValue);
        editor.putString("key_cd_sec", json);

        json = gson.toJson(clockStartTabTim);
        editor.putString("key_start", json);

        json = gson.toJson(clockSumTabTim);
        editor.putString("key_sum", json);

        editor.commit();
    }


    public void loadData() {

        ArrayList<String> listOfNamesTim = new ArrayList<>();
        ArrayList<Boolean> listOfBoolTim = new ArrayList<>();
        ArrayList<Integer> countDownSecondsValue = new ArrayList<>();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        amountOfTimers = sharedPref.getInt("amount_timers", 1);

        Gson gson = new Gson();
        String json = sharedPref.getString("key_names", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        listOfNamesTim = gson.fromJson(json, type);

        json = sharedPref.getString("key_bool", null);
        type = new TypeToken<ArrayList<Boolean>>() {
        }.getType();
        listOfBoolTim = gson.fromJson(json, type);


        json = sharedPref.getString("key_cd_sec", null);
        type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        countDownSecondsValue = gson.fromJson(json, type);

        long[] clockSumTabTim = new long[amountOfTimers];
        long[] clockStartTabTim = new long[amountOfTimers];

        json = sharedPref.getString("key_start", null);
        type = new TypeToken<long[]>() {
        }.getType();
        clockStartTabTim = gson.fromJson(json, type);

        json = sharedPref.getString("key_sum", null);
        type = new TypeToken<long[]>() {
        }.getType();
        clockSumTabTim = gson.fromJson(json, type);


        if (listOfNamesTim != null) {
            int size = amountOfTimers;
            for (int i = 0; i < size; i++) {
                if (i == 0) {       //Write values for first fragment
                    TimerFragment tim = listOfTim.get(0);
                    tim.setName(listOfNamesTim.get(0));
                    tim.running = listOfBoolTim.get(0);
                    tim.clockStart = clockStartTabTim[0];
                    tim.clockSum = clockSumTabTim[0];
                    tim.countDownValueSeconds = countDownSecondsValue.get(0);
                    tim.setTimerSeconds((int) (countDownSecondsValue.get(0) - (clockSumTabTim[0] / 1000)));
                    if(tim.running) {
                        tim.timerEndClock = tim.clockStart + tim.clockSum + (tim.countDownSeconds * 1000);
                        endTimersList.add(0, tim.timerEndClock);
                    }
                    else {
                        tim.timerEndClock = Long.MAX_VALUE;
                        endTimersList.add(0, Long.MAX_VALUE);
                    }
                } else {
                    int secondsValue = ((int) (countDownSecondsValue.get(i) - (clockSumTabTim[i] / 1000)));        // Valeu on TextView Timer
                    addTimer(listOfNamesTim.get(i), listOfBoolTim.get(i), clockStartTabTim[i], clockSumTabTim[i], countDownSecondsValue.get(i));
                }
            }
        }
        setLowestEndTimer();
    }


    // onTouchEvent is using to swipe activity
    float x1, x2, y1, y2;
    float screenDPI;
    float touchBufor;

    public boolean onTouchEvent(MotionEvent touchevent) {

        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if (x1 != 0 && x2 != 0) {

                    if (x1 > x2 + touchBufor) {
                        openAnotherActivity(true, false);
                    }
                    if (x1 + touchBufor < x2) {
                        openAnotherActivity(false, true);

                        break;
                    }
                }
        }
        return false;
    }


    StopWatchActivity stopWatchActivity = new StopWatchActivity();

    void openAnotherActivity(Boolean left, Boolean right) {
        if (left) {
            Intent intent = new Intent(this, StopWatchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (right) {
            Intent intent = new Intent(this, StopWatchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    //  Move values from Fragment to other one, it is using deleteTimer()
    void setTimerFragment(int fromID, int toID) {
        TimerFragment timBuf1, timBuf2;
        timBuf1 = listOfTim.get(fromID);
        timBuf2 = listOfTim.get(toID);

        String name = timBuf1.nameTimer;
        timBuf2.setName(name);

        timBuf2.running = timBuf1.running;
        timBuf2.clockStart = timBuf1.clockStart;
        timBuf2.clockSum = timBuf1.clockSum;
        timBuf2.timerEndClock = timBuf1.timerEndClock;
    }


    public static TimerActivity instance() {
        return INSTANCE;
    }


    ArrayList<Long> endTimersList = new ArrayList<>();
    public void setEndTimers(int id, long endTimer) {

        try {
            if(id < endTimersList.size()-1) {
                endTimersList.set(id, endTimer);
            }
            else {
                endTimersList.add(id, endTimer);
            }
        }
        catch (Exception ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
        }

    }


    //  set lowest endtimer clock on: long firstAlarmClock, and return id of this Fragment
    public void setLowestEndTimer() {
        try {
            firstAlarmClock = Long.MAX_VALUE;
            int amount = endTimersList.size();
            for (int i = 0; i < amount - 1; i++) {
                if ((endTimersList.get(i) < firstAlarmClock) && (endTimersList.get(i) != 0) &&
                        (listOfTim.get(i).running) && (endTimersList.get(i) > System.currentTimeMillis())) {
                    firstAlarmClock = endTimersList.get(i);
                    firstAlarmClockID = i;
                }
            }
        }
        catch (Exception ex) {}
    }



    public void setAlalrm(long time) {
        long bufor= System.currentTimeMillis();
        if((time <= firstAlarmClock) && (time > System.currentTimeMillis() + 1000)) {
            firstAlarmClock = Long.MAX_VALUE;

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 101, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

            ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
            PackageManager pm = this.getPackageManager();

            // turn on alarm after close app
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);


            if (checkVolume() < 30) {
                Toast.makeText(this, "Turn up the volume to hear the alarm!", Toast.LENGTH_LONG).show();
            }
        }

    }

    int checkVolume(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumePercentage = 100 * currentVolume/maxVolume;
        return currentVolumePercentage;
    }


    public void setNextAlarm() {
        setLowestEndTimer();
        setAlalrm(firstAlarmClock);
    }


    // minimize app by clicking back
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


}


