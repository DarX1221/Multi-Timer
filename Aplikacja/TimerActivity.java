package com.darasdev.multitimer;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.darasdev.multitimer.App.CHANNEL_1_ID;


public class TimerActivity extends AppCompatActivity implements SettingsTimerFragment.FragmentNameListenerTimer {
    private long firstAlarmClock = 0;
    private int firstAlarmClockID;
    private NotificationManagerCompat notificationManager;
    ArrayList<TimerFragment> listOfTim = new ArrayList();
    int lengthOfListTim = listOfTim.size();
    private static TimerActivity inst;
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
        loadData();

    }




        TimerFragment timerFragment;
        SettingsFragment settingsFragment;
        public void addTimer(String name, Boolean running, long clockStart, long clockSum, int seconds){
            timerFragment = new TimerFragment();
            FragmentTransaction fragmentTransactionTim1 = getSupportFragmentManager().beginTransaction();
            listOfTim.add(timerFragment);
            lengthOfListTim = listOfTim.size();
            fragmentTransactionTim1.add(R.id.timer_fragment, listOfTim.get(lengthOfListTim-1));
            fragmentTransactionTim1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransactionTim1.commit();

            //Setter's
            settingsFragment = new SettingsFragment();
            timerFragment.setID(lengthOfListTim-1);
            settingsFragment.setID(lengthOfListTim-1);
            timerFragment.nameTimer = name;
            timerFragment.running = running;
            //timerFragment.setTimerValue(minutes);
            timerFragment.countDownValueSeconds = seconds;
            timerFragment.clockStart = clockStart;
            timerFragment.clockSum = clockSum;
            int secondsValue = (int)(seconds - (clockSum / 1000));
            timerFragment.setTimerSeconds(secondsValue);
        }


        //FloatingButton add Fragment
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
                        setTimerFragment(i + 1, i);
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


        //Set name and timer value
        String nameTimer;
        @Override
        public void onInputNameSent(String name, String minutesStr, int id) {

            if(minutesStr != null){                             // Set Timer Value (minutes)
                int minutes = 0;
                TimerFragment timerFragmentBufor = listOfTim.get(id);
                try{
                minutes = Integer.parseInt(minutesStr);
                }
                catch(Exception ex){
                    Toast.makeText(this, "Only numbers!",Toast.LENGTH_SHORT).show();
                }
                timerFragmentBufor.setTimerSeconds(minutes*60);
            }

            if(name != null){                               //  Set name
            nameTimer = name;
            TimerFragment timerFragmentBuffor = listOfTim.get(id);
            timerFragmentBuffor.setName(name);}
        }



        //  First Fragment is create by .XML layout, setFragment() let to catch this Fragment
    Boolean firstFragment = false;
        public void setFragment(TimerFragment timerFragment) {

            if(!firstFragment) {
                if (listOfTim.size() <= 1) {
                    listOfTim.add(0, new TimerFragment());
                    listOfTim.set(0, timerFragment);
                }
            }
            firstFragment = true;
        }


        @Override
        protected void onStart(){
            super.onStart();
            inst = this;
            //loadData();
        }

        @Override
        protected void onPause(){
            super.onPause();
            saveData();
            setLowestEndTimer();
            setNotificationAlarm();
        }



        void saveData(){
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
                if (nameT == null){ nameT = "Name timer:";}
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



        public void loadData(){

            ArrayList<String> listOfNamesTim = new ArrayList<>();
            ArrayList<Boolean> listOfBoolTim = new ArrayList<>();
            ArrayList<Integer> countDownSecondsValue = new ArrayList<>();
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            amountOfTimers = sharedPref.getInt("amount_timers", 1);

            Gson gson = new Gson();
            String json = sharedPref.getString("key_names", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            listOfNamesTim = gson.fromJson(json, type);

            json = sharedPref.getString("key_bool", null);
            type = new TypeToken<ArrayList<Boolean>>() {}.getType();
            listOfBoolTim = gson.fromJson(json, type);


            json = sharedPref.getString("key_cd_sec", null);
            type = new TypeToken<ArrayList<Integer>>() {}.getType();
            countDownSecondsValue = gson.fromJson(json, type);

            long[] clockSumTabTim = new long[amountOfTimers];
            long[] clockStartTabTim = new long[amountOfTimers];

            json = sharedPref.getString("key_start", null);
            type = new TypeToken<long[]>() {}.getType();
            clockStartTabTim = gson.fromJson(json, type);

            json = sharedPref.getString("key_sum", null);
            type = new TypeToken<long[]>() {}.getType();
            clockSumTabTim = gson.fromJson(json, type);



            if(listOfNamesTim != null) { // && (listOfTim.size() != amountOfTimers)){
                int size = amountOfTimers;
                for (int i = 0 ; i < size; i++) {
                    //if(countDownTabTim == null){countDownTabTim[i] = 125;}
                    if(i==0){       //Write values for first fragment
                        TimerFragment tim = listOfTim.get(0);
                        tim.setName(listOfNamesTim.get(0));
                        tim.running = listOfBoolTim.get(0);
                        tim.clockStart = clockStartTabTim[0];
                        tim.clockSum = clockSumTabTim[0];
                        tim.countDownValueSeconds = countDownSecondsValue.get(0);
                        tim.setTimerSeconds((int)(countDownSecondsValue.get(0) - (clockSumTabTim[0]/1000)));
                    }
                    else {
                        int secondsValue = ((int)(countDownSecondsValue.get(i) - (clockSumTabTim[i]/1000)));        // Valeu on TextView Timer
                        addTimer(listOfNamesTim.get(i), listOfBoolTim.get(i), clockStartTabTim[i], clockSumTabTim[i], countDownSecondsValue.get(i));
                    }
                }
            }
        }


    float x1, x2, y1, y2;
    float touchSenstitivy = 100;
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


    StopWatchActivity stopWatchActivity = new StopWatchActivity();
    void openAnotherActivity(Boolean left, Boolean right){
        if(left){
            Intent intent = new Intent(this, StopWatchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(right){
            Intent intent = new Intent(this, StopWatchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


        //  Move values from Fragment to other one
        void setTimerFragment(int fromID, int toID){
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


        ArrayList<Long> endTimersList = new ArrayList<>();
    public void setEndTimers(int id, long endTimer){
        do{
            endTimersList.add(0L);
        }
        while(endTimersList.size() < id+2);

        endTimersList.set(id, endTimer);
            //endTimers[id] = endTimer;
            this.firstAlarmClock = endTimer;
            //setLowestEndTimer();
            endTimersList.add(0L);
    }


        //  set lowest endtimer clock on long firstAlarmClock and return id of this Fragment
    public void setLowestEndTimer(){
        amountOfTimers = listOfTim.size();
        for (int i = 0; i <= amountOfTimers; i++){
            if ((endTimersList.get(i) < firstAlarmClock) && (endTimersList.get(i) != 0)){
                firstAlarmClock = endTimersList.get(i);
                firstAlarmClockID = i;
            }
        }
    }




    String alarmName = "Alarm name";
    void alarmStart(){

        //   SEt notification
        Intent activityIntent = new Intent(this, TimerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
            notificationManager = NotificationManagerCompat.from(this);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Click me to open timers")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .setVibrate(new long[]{1000, 1000, 1000})
                    .build();
            notificationManager.notify(1, notification);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(this, alarmUri);

        //  Set MediaPlayer for Alarm
        try{
            //Thread.sleep(1500);
            mediaPlayer.start();
            Thread.sleep(5000);
        }
        catch (InterruptedException e){}
        mediaPlayer.stop();
    }





    void showAlarmFragment(Boolean show){
        AlarmFragment alarmFragment = new AlarmFragment();
        alarmFragment.setTimerActivity(this);
        FragmentTransaction fragmentTransactionAlarm = getSupportFragmentManager().beginTransaction();
        fragmentTransactionAlarm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransactionAlarm.replace(R.id.alarm_box, alarmFragment);
        if(show){
            fragmentTransactionAlarm.replace(R.id.alarm_box, alarmFragment);
        }
        if(!show){
            //mediaPlayer.stop();
            fragmentTransactionAlarm.remove(alarmFragment);
        }
            fragmentTransactionAlarm.commit();
    }






    public void setNotificationAlarm(){
        TimerFragment timerFragmentBufor = listOfTim.get(firstAlarmClockID);
        if(firstAlarmClock != 0 && (timerFragmentBufor.running)){
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent.putExtra("power", true);
        long milis = firstAlarmClock;
        alarmManager.set(AlarmManager.RTC_WAKEUP, (milis+2000), pendingIntent);
        }
    }

    public void turnOFFAlarmNotification(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }





String serializedObject(TimerActivity o){
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson2 = gsonBuilder.create();

    String json2 = gson2.toJson(o);
    return json2;
}


}

