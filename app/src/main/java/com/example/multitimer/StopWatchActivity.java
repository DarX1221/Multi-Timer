package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {
    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    int lengthOfList = listOfSW.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
    }



    StopWatchFragment stopWatchFragment;
    SettingsFragment settingsFragment;
    public void addSW(String name, Boolean running, long clockStart, long clockSum){
        stopWatchFragment = new StopWatchFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        fragmentTransaction1.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();
        //Settery
        settingsFragment = new SettingsFragment();
        stopWatchFragment.setID(lengthOfList-1);
        settingsFragment.setID(lengthOfList-1);
        stopWatchFragment.nameTimer = name;
        stopWatchFragment.running = running;
        stopWatchFragment.clockStart = clockStart;
        stopWatchFragment.clockSum = clockSum;
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

    public void deleteTimer (StopWatchFragment stopWatchFragment) {
        listOfSW.remove(stopWatchFragment);
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


    //Funkcja umozliwiająca zmianę nazwy pierwszego timera(id=0)
    String nameTimer;
    @Override
    public void onInputNameSent(String input, int id) {
        nameTimer = input;
        StopWatchFragment nameChanger = listOfSW.get(id);
        nameChanger.setName(input);
    }


    // Funkcja umożliwiająca zmianę nazwy pierwszego timera, wywołanego z kodu .XML
    public void setFragment(StopWatchFragment stopWatchFragment) {
        if (listOfSW.size() == 0) {
            listOfSW.add(stopWatchFragment);
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(this, "Load onResume", Toast.LENGTH_SHORT).show();
        loadData();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "Save onPause", Toast.LENGTH_SHORT).show();
        saveData();
    }




    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }*/

    void saveData(){
        ArrayList<String> listOfNames = new ArrayList<>();
        ArrayList<Boolean> listOfBooleans = new ArrayList<>();
        long[] clockSumTab = new long[lengthOfList];
        long[] clockStartTab = new long[lengthOfList];
        int amountOfTimers = lengthOfList;
        StopWatchFragment sw;
        for (int i = 0; i < lengthOfList; i++) {
            sw = listOfSW.get(i);

            String nameT = sw.nameTimer;
            if (nameT == null){ nameT = "naem timer saver";}
            listOfNames.add(nameT);
            listOfBooleans.add(sw.running);
            clockStartTab[i] = sw.clockStart;
            clockSumTab[i] = sw.clockSum;

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

        editor.commit();
    }

    int amountOfTimers = 0;
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

        json = sharedPref.getString("key_start", null);
        type = new TypeToken<long[]>() {}.getType();
        clockStartTab = gson.fromJson(json, type);

        json = sharedPref.getString("key_sum", null);
        type = new TypeToken<long[]>() {}.getType();
        clockSumTab = gson.fromJson(json, type);

        if(listOfNames !=null && (listOfSW.size() != amountOfTimers)){
        int size = listOfNames.size();
        for (int i = 0 ; i < size; i++) {
            if(i==0){       //Przypisanie parametrów dla pierwszego Fragment'u stworzonego przez odwołanie .XML
                StopWatchFragment sw = listOfSW.get(0);
                sw.setName(listOfNames.get(0));
                sw.running = listOfBool.get(0);
                sw.clockStart = clockStartTab[0];
                sw.clockSum = clockSumTab[0];
            }
            else {
                addSW(listOfNames.get(i), listOfBool.get(i), clockStartTab[i], 0L);
            }

        }

        }
    }
}









