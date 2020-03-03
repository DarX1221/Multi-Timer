package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {
    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    Map<StopWatchFragment, SettingsFragment> mapOfSW = new LinkedHashMap<>();
    TextView textView;

    int lengthOfList = listOfSW.size();


    StopWatchFragment stopWatchFragment1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);



        //findViewById(R.id.stopwatch_fragment).setVisibility(View.GONE);
        //Usuwa widoczności wszystkich fragmentów,

        if(savedInstanceState != null){

        }

/*
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.remove();
        fragmentTransaction.replace(R.id.stopwatch_fragment, null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
*/

        //addSW();


    }


    StopWatchFragment stopWatchFragment;
    SettingsFragment settingsFragment;
    public void addSW(){
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();

        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction1.remove(stopWatchFragment);

        stopWatchFragment = new StopWatchFragment();
        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        stopWatchFragment.setID(lengthOfList-1);
        mapOfSW.put(listOfSW.get(lengthOfList-1), settingsFragment);
        settingsFragment.setID(lengthOfList-1);


        fragmentTransaction1.replace(R.id.stopwatch_fragment, listOfSW.get(0));
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();
    }

    public void addClick(View view) {
        String nameStopWatchFragment = String.format("stopWatchFragment%d", lengthOfList);
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();


        listOfSW.add(stopWatchFragment);
        lengthOfList = listOfSW.size();
        stopWatchFragment.setID(lengthOfList-1);
        mapOfSW.put(listOfSW.get(lengthOfList-1), settingsFragment);
        settingsFragment.setID(lengthOfList-1);

        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction2.commit();
    }

    String nameTimer;
    @Override
    public void onInputNameSent(String input, int id) {
        nameTimer = input;
        StopWatchFragment nameChanger = listOfSW.get(id);
        nameChanger.setName(input);
    }


    public  void setFragment(StopWatchFragment stopWatchFragment){
        if(listOfSW.size() == 0){
            listOfSW.add(stopWatchFragment);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


}
