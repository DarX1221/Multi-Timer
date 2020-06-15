package com.darasdev.multitimer.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.SettingsFragment;
import com.darasdev.multitimer.timer.TimerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import static com.darasdev.multitimer.App.SENSITIVY_OF_TOUCHSCREEN;



public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {

    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    int lengthOfList = listOfSW.size();
    int amountOfTimers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenDPI = metrics.densityDpi;
        touchBufor = SENSITIVY_OF_TOUCHSCREEN * screenDPI;
        loadData();

    }


    // swipe screen/activity
    float x1, x2, y1, y2;
    float screenDPI;
    float touchBufor;

    public boolean onTouchEvent (MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                break;
        }

        if (x1 != 0 && x2 != 0) {

            if (x1 > x2 + touchBufor) {
                openAnotherActivity(true, false);
                //Toast.makeText(getContext(), "Left", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (x1 + touchBufor < x2) {
                //Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT).show();
                openAnotherActivity(false, true);
                return true;
            }


        /*
                if(x1 > x2 + touchSenstitivy){
                    openAnotherActivity(true, false);
                }
                if(x1 < x2 - touchSenstitivy){
                    openAnotherActivity(false, true);
                }
                break;
                */


    }
        return false;
    }


    public void openAnotherActivity(Boolean left, Boolean right){
        if(left){
            Intent intent = new Intent(StopWatchActivity.this, TimerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(right){
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    StopWatchFragment stopWatchFragment;
    SettingsFragment settingsFragment;
    public void addSW(String name, Boolean running, long clockStart, long clockSum, int sumSeconds){
        stopWatchFragment = new StopWatchFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        fragmentTransaction1.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();
        //Setter's
        settingsFragment = new SettingsFragment();
        stopWatchFragment.setID(lengthOfList-1);
        settingsFragment.setID(lengthOfList-1);
        stopWatchFragment.nameTimer = name;
        stopWatchFragment.running = running;
        stopWatchFragment.clockStart = clockStart;
        stopWatchFragment.clockSum = clockSum;
        stopWatchFragment.setSaveSeconds(sumSeconds);
    }


    public void addClick(View view) {
        String nameStopWatchFragment = String.format("stopWatchFragment%d", lengthOfList);
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();
        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        stopWatchFragment.setID(lengthOfList - 1);
        settingsFragment.setID(lengthOfList - 1);

        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList - 1));
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction2.commit();
    }


    public void deleteTimer (int id, StopWatchFragment stopWatchFragment) {
        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
        StopWatchFragment swF;
        if((listOfSW.size()>1)) {
            if (id == 0) {
                int size = listOfSW.size();
                for (int i = 0; i < size - 1; i++) {
                    setSWFragment(i + 1, i);
                    listOfSW.get(i).setChronometer();
                    int bufor = listOfSW.get(i).saveSeconds;
                    listOfSW.get(i).setSaveSecondsTextView(bufor);
                }
                swF = listOfSW.get(size - 1);
                swF.running = false;
                listOfSW.remove(swF);
                fragmentTransaction3.remove(swF);

            } else {
                fragmentTransaction3.remove(stopWatchFragment);

                swF = listOfSW.get(id);
                swF.running = false;
                listOfSW.remove(id);
                lengthOfList = listOfSW.size();


                for (int i = 0; i < lengthOfList; i++) { //change ID of Fragment's
                    swF = listOfSW.get(i);
                    swF.setID(i);
                }
            }
            fragmentTransaction3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction3.commit();
        }
    }


    //Set name and timer value
    String nameTimer;
    @Override
    public void onInputNameSent(String input, int id) {
        nameTimer = input;
        StopWatchFragment nameChanger = listOfSW.get(id);
        nameChanger.setName(input);
    }



    //  First Fragment is create by .XML layout of Activity, setFragment() let to catch this Fragment
    Boolean firstFragment = false;
    public void setFragment(StopWatchFragment stopWatchFragment) {
        if(!firstFragment) {
            if (listOfSW.size() <= 1) {
                listOfSW.add(0, stopWatchFragment);
                listOfSW.set(0, stopWatchFragment);
            }
        }
        firstFragment = true;
    }


    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveData();
    }


    void saveData(){
        ArrayList<String> listOfNames = new ArrayList<>();
        ArrayList<Boolean> listOfBooleans = new ArrayList<>();
        int amountOfTimers = listOfSW.size();
        long[] clockSumTab = new long[amountOfTimers];
        long[] clockStartTab = new long[amountOfTimers];
        int[] sumSecondsTab = new int[amountOfTimers];

        StopWatchFragment sw;
        for (int i = 0; i < amountOfTimers; i++) {
            sw = listOfSW.get(i);

            String nameT = sw.nameTimer;
            if (nameT == null){ nameT = "Name Timer:";}
            listOfNames.add(nameT);
            listOfBooleans.add(sw.running);
            clockStartTab[i] = sw.clockStart;
            clockSumTab[i] = sw.clockSum;
            sumSecondsTab[i] = sw.getSaveSeconds();

        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("amount_timers", amountOfTimers);

        Gson gson = new Gson();
        String json = gson.toJson(listOfNames);
        editor.putString("key_names", json);

        json = gson.toJson(listOfBooleans);
        editor.putString("key_bool", json);

        json = gson.toJson(clockStartTab);
        editor.putString("key_start", json);

        json = gson.toJson(clockSumTab);
        editor.putString("key_sum", json);

        json = gson.toJson(sumSecondsTab);
        editor.putString("sum_seconds_tab", json);

        editor.commit();
    }


    public void loadData(){
        ArrayList<String> listOfNames = new ArrayList<>();
        ArrayList<Boolean> listOfBool = new ArrayList<>();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        amountOfTimers = sharedPref.getInt("amount_timers", 1);

        Gson gson = new Gson();
        String json = sharedPref.getString("key_names", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listOfNames = gson.fromJson(json, type);

        json = sharedPref.getString("key_bool", null);
        type = new TypeToken<ArrayList<Boolean>>() {}.getType();
        listOfBool = gson.fromJson(json, type);

        long[] clockSumTab = new long[amountOfTimers];
        long[] clockStartTab = new long[amountOfTimers];
        int[] sumSecondsTab = new int[amountOfTimers];

        json = sharedPref.getString("key_start", null);
        type = new TypeToken<long[]>() {}.getType();
        clockStartTab = gson.fromJson(json, type);

        json = sharedPref.getString("key_sum", null);
        type = new TypeToken<long[]>() {}.getType();
        clockSumTab = gson.fromJson(json, type);

        json = sharedPref.getString("sum_seconds_tab", null);
        type = new TypeToken<int[]>() {}.getType();
        sumSecondsTab = gson.fromJson(json, type);


        if((listOfNames != null)) {
        int size = amountOfTimers;
        for (int i = 0 ; i < size; i++) {
            if(i<1){
                /*
                    if(i<1) it is for first Fragment create by XML file of Activity
                    You cant use addSW because you create another Fragment
                 */
                StopWatchFragment sw = listOfSW.get(0);

                sw.setName(listOfNames.get(0));
                sw.running = listOfBool.get(0);
                sw.clockStart = clockStartTab[0];
                sw.clockSum = clockSumTab[0];
                sw.setTimer(clockSumTab[0]);
                sw.setSaveSeconds(sumSecondsTab[0]);
                sw.setSaveSecondsTextView(sumSecondsTab[0]);
            }
            else {      //create another Fragment's from save
                addSW(listOfNames.get(i), listOfBool.get(i), clockStartTab[i], clockSumTab[i], sumSecondsTab[i]);
            }

        }

        }
    }

    // moving parameters from one Fragment to another
    void setSWFragment(int fromID, int toID){
        StopWatchFragment swBuf1, swBuf2;
        swBuf1 = listOfSW.get(fromID);
        swBuf2 = listOfSW.get(toID);

        String name = swBuf1.nameTimer;
        swBuf2.setName(name);

        swBuf2.running = swBuf1.running;
        swBuf2.clockStart = swBuf1.clockStart;
        swBuf2.clockSum = swBuf1.clockSum;
        swBuf2.setSaveSeconds(swBuf1.getSaveSeconds());
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

