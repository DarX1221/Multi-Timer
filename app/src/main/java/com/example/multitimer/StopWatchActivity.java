package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {
    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    int lengthOfList = listOfSW.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        //loadData();

        /*if(savedInstanceState != null){

        }*/
        //addSW();
    }

    StopWatchFragment stopWatchFragment;
    SettingsFragment settingsFragment;
    /*
    public void addSW(){
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        stopWatchFragment = new StopWatchFragment();
        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        stopWatchFragment.setID(lengthOfList-1);
        settingsFragment.setID(lengthOfList-1);
        fragmentTransaction1.replace(R.id.stopwatch_fragment, listOfSW.get(0));
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();
    }*/

    public void addClick(View view) {
        String nameStopWatchFragment = String.format("stopWatchFragment%d", lengthOfList);
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();
        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        stopWatchFragment.setID(lengthOfList-1);
        settingsFragment.setID(lengthOfList-1);

        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction2.commit();
    }

    //Funkcja umozliwiająca zmianę nazwy timera
    String nameTimer;
    @Override
    public void onInputNameSent(String input, int id) {
        nameTimer = input;
        StopWatchFragment nameChanger = listOfSW.get(id);
        nameChanger.setName(input);
    }

    // Funkcja umożliwiająca zmianę nazwy pierwszego timera, wywołanego z kodu .XML
    public  void setFragment(StopWatchFragment stopWatchFragment){
        if(listOfSW.size() == 0){
            listOfSW.add(stopWatchFragment);
        }
    }


    // Saver
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //saveData();

        //savedInstanceState.putLi
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfSW);
        editor.putString("listJS", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("listJS", null);
        Type type = new TypeToken<ArrayList<SettingsFragment>>() {}.getType();
        listOfSW = gson.fromJson(json, type);

        if(listOfSW == null) {
            listOfSW = new ArrayList<>();
        }
    }


}
