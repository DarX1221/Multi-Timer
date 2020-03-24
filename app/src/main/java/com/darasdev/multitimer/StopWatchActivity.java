package com.darasdev.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {
    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    int lengthOfList = listOfSW.size();
    int amountOfTimers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        loadData();
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


    public void deleteTimer (int id, StopWatchFragment stopWatchFragment) {
        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
        StopWatchFragment swF;
        if((listOfSW.size()>1)) {
            if (id == 0) {
                int size = listOfSW.size();
                for (int i = 0; i < size - 1; i++) {
                    setSWFragment(i + 1, i);
                    listOfSW.get(i).setChronometer();
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


                for (int i = 0; i < lengthOfList; i++) { //zamiana id Fragment'ów
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



    //  First Fragment is create by .XML layout, setFragment() let to catch this Fragment
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

        StopWatchFragment sw;
        for (int i = 0; i < amountOfTimers; i++) {
            sw = listOfSW.get(i);

            String nameT = sw.nameTimer;
            if (nameT == null){ nameT = "Name Timer:";}
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


        if((listOfNames != null)) { // && (listOfSW.size() != amountOfTimers)){
        int size = amountOfTimers;
        for (int i = 0 ; i < size; i++) {
            if(i<1){       //Przypisanie parametrów dla pierwszego Fragment'u stworzonego przez odwołanie .XML
                            //  Nie można użyć metody addSW(), gdyż stworzy ona kolejny StopWatchFragment
                //listOfSW.set(0, new StopWatchFragment());
                StopWatchFragment sw = listOfSW.get(0);

                sw.setName(listOfNames.get(0));
                sw.running = listOfBool.get(0);
                sw.clockStart = clockStartTab[0];
                sw.clockSum = clockSumTab[0];
                sw.setTimer(clockSumTab[0]);
            }
            else {      //Stworzenie zapisanych Fragmentów
                addSW(listOfNames.get(i), listOfBool.get(i), clockStartTab[i], clockSumTab[i]);
            }

        }

        }
    }

    // Funkcja umożliwiająca przeniesienie parametrów Stopera(Fragment'u)
    void setSWFragment(int fromID, int toID){
        StopWatchFragment swBuf1, swBuf2;
        swBuf1 = listOfSW.get(fromID);
        swBuf2 = listOfSW.get(toID);

        String name = swBuf1.nameTimer;
        swBuf2.setName(name);

        swBuf2.running = swBuf1.running;
        swBuf2.clockStart = swBuf1.clockStart;
        swBuf2.clockSum = swBuf1.clockSum;
    }
}









