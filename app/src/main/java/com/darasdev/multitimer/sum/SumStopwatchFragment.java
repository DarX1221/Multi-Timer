package com.darasdev.multitimer.sum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.darasdev.multitimer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SumStopwatchFragment extends Fragment implements View.OnClickListener {
    TextView sumName, sumTextView;
    SumStopwatchActivity sumStopwatchActivity;

    public SumStopwatchFragment() {
        //System.out.println("Frag");
        //Toast.makeText(getContext(), "Frag", Toast.LENGTH_SHORT).show();
        //setName(name);
        //setSeconds(seconds);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sum_stopwatch, container, false);

        //textView = view.findViewById(R.id.stop_watch_name);
        sumName = view.findViewById(R.id.sum_name);
        sumTextView = view.findViewById(R.id.sum_timer_text);
        Button resetButton = (Button) view.findViewById(R.id.sum_restart_button);
        resetButton.setOnClickListener(this);
        Button deleteButton = (Button) view.findViewById(R.id.sum_delete_button);
        deleteButton.setOnClickListener(this);

        sumStopwatchActivity = (SumStopwatchActivity) getActivity();
        sumStopwatchActivity.setFragment(this);
        //Odwołanie do aktywności, umożliwia przechwycenie adresu pierwszego fragmentu
        //stopWatchActivity = (StopWatchActivity) getActivity();
        //stopWatchActivity.setFragment(this);


        sumName.setText(name);
        sumTextView.setText(secondsToTime(seconds));

        Toast.makeText(getContext(), "Create fragment view", Toast.LENGTH_SHORT).show();
        return  view;
    }

    public void setActivity(SumStopwatchActivity activity) {
        sumStopwatchActivity = activity;
    }



    // swipe screen/activity
    private float x1, x2, y1, y2, xm, ym;
    private float touchSenstitivy = 75;
    boolean shouldClick =true;
    // @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                shouldClick = true;
                break;

            case MotionEvent.ACTION_UP:
                if(shouldClick){
                    v.performClick();
                }
                x2 = event.getX();
                y2 = event.getY();
                break;

            case MotionEvent.ACTION_BUTTON_PRESS:
        }

        if((x1 > x2) & (x1 > x2 + touchSenstitivy) & (x2 != 0)){
            //stopWatchActivity.openAnotherActivity(true, false);
            Toast.makeText(sumStopwatchActivity, "Move", Toast.LENGTH_SHORT).show();
        }
        if((x1 < x2) & (x1 + touchSenstitivy < x2) & (x2 != 0)){
            //stopWatchActivity.openAnotherActivity(false, true);
            Toast.makeText(sumStopwatchActivity, "Move", Toast.LENGTH_SHORT).show();
        }
        return true;
    }




    private int seconds;
    public void setSeconds(int seconds){
        this.seconds = seconds;
        //sumTextView.setText(secondsToTime(seconds));
    }

    public int getSeconds() {
        return seconds;
    }

    private String secondsToTime(int seconds){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        String stopWatchTime = String.format("%02d:%02d:%02d", hours, minutes, secs);
        return stopWatchTime;
    }

    public void resetChronometer(View view) {
        seconds = 0;
        sumTextView.setText("00:00:00");
    }

    public void deleteStopWatch(){
        SumStopwatchActivity sswAct = getStopWatchActivity();
        sswAct.deleteTimer(getID(), this);
    }

    // each Fragment has own ID in Activity
    int idSSW;
    public void setID(int id){   idSSW = id;}
    public int getID(){     return idSSW; }

    String name = "Name Timer:";
    public void setName(String name) {
        this.name = name;
        //sumName.setText(name);
    }
    public String getName() {
        return name;
    }

    public SumStopwatchActivity getStopWatchActivity() {
        return sumStopwatchActivity;
    }


    // React fo buttons ()
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.reset_button:
                resetChronometer(view);
                break;

            case R.id.delete_button :
                deleteStopWatch();
                break;
        }
    }











}
