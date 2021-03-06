package com.darasdev.multitimer.timer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.darasdev.multitimer.R;
import com.darasdev.multitimer.timer.TimerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsTimerFragment extends Fragment {
    private EditText editText, setTimerEditText;
    private Button setNameButton, deleteButton,  setTimerButton;

    public interface FragmentNameListenerTimer {
        void onInputNameSent(String input, String minutes, int id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_timer, container, false);
        editText = view.findViewById(R.id.editText);
        setTimerEditText = view.findViewById(R.id.set_timer_edittext);

        //  Set buttons and listener's
        //  Button set timer
        setTimerButton = view.findViewById(R.id.set_timer_button);
        setTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int minutes = setTimerEditText.getI

                String minutes = setTimerEditText.getText().toString();
                listener.onInputNameSent(null, minutes, getID()); }});

        //  Button set name
        setNameButton = view.findViewById(R.id.setname_button);
        setNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                listener.onInputNameSent(input, null, getID()); }});

        //Button delete parent(Timer) Fragment
        deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFragment();
            }
        });

        return view;
    }

    private FragmentNameListenerTimer listener = new FragmentNameListenerTimer() {
        @Override
        public void onInputNameSent(String name, String minutes, int id) {
            if(minutes != null){
                String timerValue = setTimerEditText.getText().toString();
                listener.onInputNameSent(null, timerValue, id);
            }
            if(name != null){
            String nameBufor = editText.getText().toString();
            listener.onInputNameSent(nameBufor, null, id);} }
    };






            @Override
            public void onAttach(Context context){
                super.onAttach(context);
                if(context instanceof FragmentNameListenerTimer){
                    listener = (FragmentNameListenerTimer) context;
                }
                else {throw new RuntimeException(context.toString() +
                        "implement FragmentNameListenerTimer");
                    }
            }

            // Saver
            @Override
            public void onSaveInstanceState(Bundle savedInstanceState){

            }

            @Override
            public void onDetach(){
                super.onDetach();
                listener = null;
            }

            int idSet;
            void setID(int id){
                idSet = id;
            }
            public int getID(){
                return idSet;
            }

            //Switch type, Stopwatch = true, Timer = False
            Boolean type;
            void setType(Boolean type){this.type = type;}
            Boolean getType(){return type;}

            void deleteFragment(){
                    TimerFragment timF = (TimerFragment) getParentFragment();
                    timF.deleteTimer();
            }

        }






