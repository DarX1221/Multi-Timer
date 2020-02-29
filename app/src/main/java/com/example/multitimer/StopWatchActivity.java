package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {
    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    Map<SettingsFragment, StopWatchFragment> mapOfSW = new HashMap<>();
    TextView textView;
    StopWatchFragment stopWatchFragment;
    SettingsFragment settingsFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        if(savedInstanceState != null){

        }

        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction1.remove(stopWatchFragment);
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();




/*
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();

        listOfSW.add(stopWatchFragment);
        int lengthOfList = listOfSW.size();
        mapOfSW.put(settingsFragment, listOfSW.get(lengthOfList-1));
        stopWatchFragment.setSettingFragment(settingsFragment);

        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.remove(stopWatchFragment);
        fragmentTransaction1.replace(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();*/


        addSW();


/*
        listOfSW.add(new StopWatchFragment());
        mapOfSW.put(new SettingsFragment(), listOfSW.get(0));
        StopWatchFragment stopWatchFragment = (StopWatchFragment) listOfSW.get(0);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(stopWatchFragment);
        fragmentTransaction.replace(R.id.stopwatch_fragment, stopWatchFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();*/

    }



    public void addSW(){
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();

        listOfSW.add(stopWatchFragment);
        int lengthOfList = listOfSW.size();
        mapOfSW.put(settingsFragment, listOfSW.get(lengthOfList-1));
        stopWatchFragment.setSettingFragment(settingsFragment);

        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.remove(stopWatchFragment);
        fragmentTransaction1.replace(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction1.commit();
    }

    public void addClick(View view) {
        stopWatchFragment = new StopWatchFragment();
        settingsFragment = new SettingsFragment();

        listOfSW.add(stopWatchFragment);
        int lengthOfList = listOfSW.size();
        mapOfSW.put(settingsFragment, listOfSW.get(lengthOfList-1));
        stopWatchFragment.setSettingFragment(settingsFragment);


        //listOfSW.add(new StopWatchFragment());
        //int lengthOfList = listOfSW.size();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction2.commit();
    }

    String nameTimer;
    @Override
    public void onInputNameSent(String input, SettingsFragment settingsFragment) {
        nameTimer = input;
        StopWatchFragment nameChanger = mapOfSW.get(settingsFragment);
        nameChanger.setName(input);

        //textView = (TextView) findViewById(R.id.stop_watch_name);
        //textView.setText(input);
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


}
