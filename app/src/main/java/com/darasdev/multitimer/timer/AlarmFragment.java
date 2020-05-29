package com.darasdev.multitimer.timer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.timer.TimerActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {
    TextView textViewAlarm;
    TimerActivity timerActivity;

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        Button offAlarm = (Button) view.findViewById(R.id.button_turnoff_alarm);

        textViewAlarm = view.findViewById(R.id.alarm_name_text_view);
        setAlarmName();
        return view;
    }



    void setTimerActivity(TimerActivity timerActivity){
        this.timerActivity = timerActivity;
    }
    TimerActivity getTimerActivity(){
        return timerActivity;
    }

    void setAlarmName(){
        String name = timerActivity.alarmName;
        textViewAlarm.setText(name);
    }


}
