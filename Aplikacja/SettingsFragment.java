package com.darasdev.multitimer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private EditText editText;
    private Button setNameButton, deleteButton;


    public interface FragmentNameListener {
        void onInputNameSent(String input, int id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        editText = view.findViewById(R.id.editText);

        //Przypisanie przycisków, listener'ów oraz funkcji wywołanych po kliknięciu
        // Button set name
        setNameButton = view.findViewById(R.id.setname_button);
        setNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                listener.onInputNameSent(input, getID()); }});
        // Button delete parent (StopWatch) Fragment
        deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFragment();
            }
        });

        return view;
    }
    private FragmentNameListener listener = new FragmentNameListener() {
        @Override
        public void onInputNameSent(String input, int id) {
            String name = editText.getText().toString();
            listener.onInputNameSent(name, id);
        }


    };



    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof FragmentNameListener){
            listener = (FragmentNameListener) context;
        }else {throw new RuntimeException(context.toString() +
                "implement FragmentNameListener");}
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

    //  Switch type, Stopwatch = true, Timer = False
    Boolean type;
    void setType(Boolean type){this.type = type;}
    Boolean getType(){return type;}

    void deleteFragment(){
        if(type) {
            StopWatchFragment swF = (StopWatchFragment) getParentFragment();
            swF.deleteStopWatch();
        }
        else {
            TimerFragment timF = (TimerFragment) getParentFragment();
            timF.deleteTimer();
        }
    }

}
