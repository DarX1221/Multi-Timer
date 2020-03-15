package com.example.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;



public class TimerActivity extends AppCompatActivity implements SettingsTimerFragment.FragmentNameListenerTimer {
    ArrayList<TimerFragment> listOfTim = new ArrayList();
    int lengthOfListTim = listOfTim.size();
    private static TimerActivity inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
    }

        TimerFragment timerFragment;
        SettingsFragment settingsFragment;
        public void addTimer(String name, Boolean running, long clockStart, long clockSum){
            timerFragment = new TimerFragment();
            FragmentTransaction fragmentTransactionTim1 = getSupportFragmentManager().beginTransaction();
            listOfTim.add(timerFragment);
            lengthOfListTim = listOfTim.size();
            fragmentTransactionTim1.add(R.id.timer_fragment, listOfTim.get(lengthOfListTim-1));
            fragmentTransactionTim1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransactionTim1.commit();
            //Settery
            settingsFragment = new SettingsFragment();
            timerFragment.setID(lengthOfListTim-1);
            settingsFragment.setID(lengthOfListTim-1);
            timerFragment.nameTimer = name;
            timerFragment.running = running;
            timerFragment.clockStart = clockStart;
            timerFragment.clockSum = clockSum;
        }

        public void addClick(View view) {
            timerFragment = new TimerFragment();
            settingsFragment = new SettingsFragment();
            listOfTim.add(timerFragment);
            lengthOfListTim = listOfTim.size();
            timerFragment.setID(lengthOfListTim - 1);
            settingsFragment.setID(lengthOfListTim - 1);

            FragmentTransaction fragmentTransactionTim2 = getSupportFragmentManager().beginTransaction();
            fragmentTransactionTim2.add(R.id.timer_fragment, listOfTim.get(lengthOfListTim - 1));
            fragmentTransactionTim2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransactionTim2.commit();
        }

        public void deleteTimer (int id, TimerFragment timerFragment) {
            FragmentTransaction fragmentTransactionTim3 = getSupportFragmentManager().beginTransaction();
            TimerFragment timF;
            if((listOfTim.size()>1)) {
                if (id == 0) {
                    int size = listOfTim.size();
                    for (int i = 0; i < size - 1; i++) {
                        setTimFragment(i + 1, i);
                        listOfTim.get(i).setChronometer();
                    }
                    timF = listOfTim.get(size - 1);
                    timF.running = false;
                    fragmentTransactionTim3.remove(timF);
                    listOfTim.remove(timF);

                } else {  // Usunięcie Fragmentu Stopera (id!=0)
                    fragmentTransactionTim3.remove(timerFragment);

                    timF = listOfTim.get(id);
                    timF.running = false;
                    listOfTim.remove(timerFragment);
                    lengthOfListTim = listOfTim.size();


                    for (int i = 0; i < lengthOfListTim; i++) { //zamiana id Fragment'ów
                        timF = listOfTim.get(i);
                        timF.setID(i);
                    }
                }
                fragmentTransactionTim3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransactionTim3.commit();
            }
        }

        //Funkcja umozliwiająca zmianę nazwy oraz wartości timera
        String nameTimer;
        @Override
        public void onInputNameSent(String name, String minutesStr, int id) {
            if(minutesStr != null){
                TimerFragment timerFragmentBufor = listOfTim.get(id);
                int minutes = Integer.parseInt(minutesStr);
                timerFragmentBufor.setTimerValue(minutes);
            }
            if(name != null){
            nameTimer = name;
            TimerFragment timerFragmentBuffor = listOfTim.get(id);
            timerFragmentBuffor.setName(name);}
        }

        // Funkcja umożliwiająca zmianę nazwy pierwszego timera, wywołanego z kodu .XML
        public void setFragment(TimerFragment timerFragment) {
            if (listOfTim.size() == 0) {
                listOfTim.add(timerFragment);
            }
        }


        @Override
        protected void onStart(){
            super.onStart();
            inst = this;
            loadData();
        }

        @Override
        protected void onPause(){
            super.onPause();
            saveData();
        }


        void saveData(){
            ArrayList<String> listOfNamesTim = new ArrayList<>();
            ArrayList<Boolean> listOfBooleansTim = new ArrayList<>();
            long[] clockSumTabTim = new long[lengthOfListTim];
            long[] clockStartTabTim = new long[lengthOfListTim];
            int amountOfTimers = listOfTim.size();
            TimerFragment tim;
            for (int i = 0; i < amountOfTimers; i++) {
                tim = listOfTim.get(i);

                String nameT = tim.nameTimer;
                if (nameT == null){ nameT = "name timer saver";}
                listOfNamesTim.add(nameT);
                listOfBooleansTim.add(tim.running);
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

            json = gson.toJson(clockStartTabTim);
            editor.putString("key_start", json);

            json = gson.toJson(clockSumTabTim);
            editor.putString("key_sum", json);

            editor.commit();
        }

        int amountOfTimers = 0;
        public void loadData(){
            ArrayList<String> listOfNamesTim = new ArrayList<>();
            ArrayList<Boolean> listOfBoolTim = new ArrayList<>();
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            amountOfTimers = sharedPref.getInt("amount_timers", 1);

            Gson gson = new Gson();
            String json = sharedPref.getString("key_names", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            listOfNamesTim = gson.fromJson(json, type);

            json = sharedPref.getString("key_bool", null);
            type = new TypeToken<ArrayList<Boolean>>() {}.getType();
            listOfBoolTim = gson.fromJson(json, type);

            long[] clockSumTabTim = new long[amountOfTimers];
            long[] clockStartTabTim = new long[amountOfTimers];

            json = sharedPref.getString("key_start", null);
            type = new TypeToken<long[]>() {}.getType();
            clockStartTabTim = gson.fromJson(json, type);

            json = sharedPref.getString("key_sum", null);
            type = new TypeToken<long[]>() {}.getType();
            clockSumTabTim = gson.fromJson(json, type);

            if(listOfNamesTim !=null && (listOfTim.size() != amountOfTimers)){
                int size = listOfNamesTim.size();
                for (int i = 0 ; i < size; i++) {
                    if(i==0){       //Przypisanie parametrów dla pierwszego Fragment'u stworzonego przez odwołanie .XML
                        TimerFragment tim = listOfTim.get(0);
                        tim.setName(listOfNamesTim.get(0));
                        tim.running = listOfBoolTim.get(0);
                        tim.clockStart = clockStartTabTim[0];
                        tim.clockSum = clockSumTabTim[0];
                        tim.setTimer(clockSumTabTim[0]);
                    }
                    else {
                        addTimer(listOfNamesTim.get(i), listOfBoolTim.get(i), clockStartTabTim[i], clockSumTabTim[i]);
                    }
                }
            }
        }

        // Funkcja umożliwiająca przeniesienie parametrów Stopera(Fragment'u)
        void setTimFragment(int fromID, int toID){
            TimerFragment timBuf1, timBuf2;
            timBuf1 = listOfTim.get(fromID);
            timBuf2 = listOfTim.get(toID);

            String name = timBuf1.nameTimer;
            timBuf2.setName(name);

            timBuf2.running = timBuf1.running;
            timBuf2.clockStart = timBuf1.clockStart;
            timBuf2.clockSum = timBuf1.clockSum;
        }

        public static TimerActivity instance(){
            return inst;
        }


    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    MediaPlayer mediaPlayer;

    FragmentTransaction fragmentTransactionAlarm = getSupportFragmentManager().beginTransaction();
    AlarmFragment alarmFragment = new AlarmFragment();
    String alarmName = "Alarm name Activ";
    void alarm(Boolean power){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        fragmentTransactionAlarm.replace(R.id.alarm_box, alarmFragment);


        //Audio



        mediaPlayer = MediaPlayer.create(this, alarmUri);

        //  ON alarm
        if(power) {
            mediaPlayer.start();

            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);

            fragmentTransactionAlarm.replace(R.id.alarm_box, alarmFragment);
            fragmentTransactionAlarm.commit();
            alarmFragment.setTimerActivity(this);


            Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), alarmUri);
            //ringtone.play();
        }
        //  Off alarm
        if(!power){
            mediaPlayer.setLooping(false);
            mediaPlayer.pause();
            mediaPlayer.stop();
            //Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), alarmUri);
            //ringtone.stop();
            //alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Stop!kkjhkjlh", Toast.LENGTH_SHORT).show();

            fragmentTransactionAlarm.remove(alarmFragment);
            fragmentTransactionAlarm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);


        }
        //fragmentTransactionAlarm.commit();
    }

    void alarmStop(){

        mediaPlayer.setLooping(false);
        mediaPlayer.pause();
        mediaPlayer.stop();
        //Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), alarmUri);
        //ringtone.stop();
        //alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Stop!kkjhkjlh", Toast.LENGTH_SHORT).show();

        fragmentTransactionAlarm.remove(alarmFragment);
        fragmentTransactionAlarm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);



        fragmentTransactionAlarm.commit();



    }
}

