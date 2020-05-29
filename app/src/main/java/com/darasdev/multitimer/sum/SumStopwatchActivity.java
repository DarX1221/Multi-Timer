package com.darasdev.multitimer.sum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.timer.TimerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SumStopwatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_stopwatch);
        loadData();
        //addSSW("Some timer 3", 3);
        addSSW("Some timer 15", 15);
        addSSW("Some timer 30", 30);

    }



    // odpowiada za otwieranie kolejnych activity po przesunięciu palcem
    float touchSenstitivy = 100;
    float x1, x2, y1, y2;
    public boolean onTouchEvent (MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if(x1 > x2 + touchSenstitivy){
                    openAnotherActivity(true, false);
                }
                if(x1 < x2 - touchSenstitivy){
                    openAnotherActivity(false, true);
                }
                break;
        }
        return false;
    }

    public void openAnotherActivity(Boolean left, Boolean right){
        if(left){
            Intent intent = new Intent(SumStopwatchActivity.this, TimerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(right){
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }





    // listOfSSW służy do przehowywania fragmentów wyśiwetlających sum
    SumStopwatchActivity activitySSW;
    SumStopwatchFragment fragmentSSW;
    ArrayList<SumStopwatchFragment> listOfSSWFragment = new ArrayList<>();
    int lengthOfFragmentList;

    public void addSSW(String name, int seconds) {
        fragmentSSW = new SumStopwatchFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        listOfSSWFragment.add(fragmentSSW);
        lengthOfFragmentList = listOfSSWFragment.size();

        fragmentTransaction.add(R.id.sum_stop_watch_fragment, listOfSSWFragment.get(lengthOfFragmentList - 1));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        fragmentSSW.setName(name);
        fragmentSSW.setSeconds(seconds);

    }




    public void deleteTimer (int id, SumStopwatchFragment sumStopwatchFragment) {
        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
        SumStopwatchFragment sswF;
        if((listOfSSWFragment.size()>1)) {
            if (id == 0) {
                int size = listOfSSWFragment.size();
                for (int i = 0; i < size - 1; i++) {
                    setSWFragment(i + 1, i);
                    //listOfSSWFragment.get(i).setSeconds();
                }
                sswF = listOfSSWFragment.get(size - 1);

                listOfSSWFragment.remove(sswF);
                fragmentTransaction3.remove(sswF);

            } else {
                fragmentTransaction3.remove(sumStopwatchFragment);

                sswF = listOfSSWFragment.get(id);

                listOfSSWFragment.remove(id);
                lengthOfFragmentList = listOfSSWFragment.size();


                for (int i = 0; i < lengthOfFragmentList; i++) { //zamiana id Fragment'ów
                    sswF = listOfSSWFragment.get(i);
                    sswF.setID(i);
                }
            }
            fragmentTransaction3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction3.commit();
        }
    }


    // Funkcja umożliwiająca przeniesienie parametrów Stopera(Fragment'u)
    void setSWFragment(int fromID, int toID){
        SumStopwatchFragment sswBuf1, sswBuf2;
        sswBuf1 = listOfSSWFragment.get(fromID);
        sswBuf2 = listOfSSWFragment.get(toID);

        String name = sswBuf1.getName();
        sswBuf2.setName(name);
        sswBuf2.setSeconds(sswBuf1.getSeconds());
    }




    void saveData(){
        ArrayList<String> listOfNames = new ArrayList<>();

        int amountOfTimers = listOfSSWFragment.size();
        int[] clockSumTab = new int[amountOfTimers];

        SumStopwatchFragment ssw;
        for (int i = 0; i < amountOfTimers; i++) {
            ssw = listOfSSWFragment.get(i);

            String nameT = ssw.getName();
            if (nameT == null){ nameT = "Name Timer:";}
            listOfNames.add(nameT);
            clockSumTab[i] = ssw.getSeconds();

        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("amount_timers", amountOfTimers);

        Gson gson = new Gson();
        String json = gson.toJson(listOfNames);
        editor.putString("key_names", json);

        json = gson.toJson(clockSumTab);
        editor.putString("key_sum", json);

        editor.commit();
    }


    public void loadData(){
        ArrayList<String> listOfNames = new ArrayList<>();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int amountOfTimers = sharedPref.getInt("amount_timers", 1);

        Gson gson = new Gson();
        String json = sharedPref.getString("key_names", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listOfNames = gson.fromJson(json, type);

        int[] clockSumTab = new int[amountOfTimers];

        json = sharedPref.getString("key_sum", null);
        type = new TypeToken<long[]>() {}.getType();
        clockSumTab = gson.fromJson(json, type);

        if((listOfNames != null)) {
            int size = amountOfTimers;
            for (int i = 0 ; i < size-1; i++) {
                if(i<1){       //Przypisanie parametrów dla pierwszego Fragment'u stworzonego przez odwołanie .XML
                    //  Nie można użyć metody addSW(), gdyż stworzy ona kolejny StopWatchFragment

                    SumStopwatchFragment ssw = listOfSSWFragment.get(0);

                    ssw.setName(listOfNames.get(0));
                    ssw.setSeconds(clockSumTab[0]);
                }
                else {      //Stworzenie zapisanych Fragmentów
                    addSSW(listOfNames.get(i), clockSumTab[i]);
                }

            }

        }
    }






    //  First Fragment is create by .XML layout of Activity, setFragment() let to catch this Fragment
    Boolean firstFragment = false;
    public void setFragment(SumStopwatchFragment sumStopwatchFragment) {
        if(!firstFragment) {
            if (listOfSSWFragment.size() <= 1) {
                listOfSSWFragment.add(0, sumStopwatchFragment);
                listOfSSWFragment.set(0, sumStopwatchFragment);
            }
        }
        firstFragment = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }






















}
