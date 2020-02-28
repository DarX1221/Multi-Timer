package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

public class StopWatchActivity extends AppCompatActivity implements SettingsFragment.FragmentNameListener {
    ArrayList<StopWatchFragment> listOfSW = new ArrayList();
    private SettingsFragment settingsFragment;
    TextView textView;

    public void addSW(){
        listOfSW.add(new StopWatchFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        if(savedInstanceState != null){

        }

        textView = (TextView) findViewById(R.id.stop_watch_name);
        settingsFragment = new SettingsFragment();
        StopWatchFragment stopWatchFragment = new StopWatchFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(stopWatchFragment);
        fragmentTransaction.replace(R.id.stopwatch_fragment, stopWatchFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }




    public void addClick(View view) {
        listOfSW.add(new StopWatchFragment());
        int lengthOfList = listOfSW.size();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.stopwatch_fragment, listOfSW.get(lengthOfList-1));
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction2.commit();
    }

    String nameTimer;
    @Override
    public void onInputNameSent(String input) {
        nameTimer = input;
        textView.setText(input);
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


}
