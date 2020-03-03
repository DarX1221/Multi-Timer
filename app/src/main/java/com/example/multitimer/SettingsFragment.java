package com.example.multitimer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private EditText editText;
    private Button setNameButton;
    SettingsFragment settingsFragment;



    public interface FragmentNameListener {
        void onInputNameSent(String input, int id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        String idS = String.valueOf(getID());
        Toast.makeText(getContext(),idS, Toast.LENGTH_SHORT).show();

        TextView textView ;
        editText = view.findViewById(R.id.editText);
        setNameButton = view.findViewById(R.id.setname_button);
        setNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                //settingsFragment = setSetter(settingsFragment);

                listener.onInputNameSent(input, getID());
            }
        });
        return view;
    }



    private FragmentNameListener listener = new FragmentNameListener() {
        @Override
        public void onInputNameSent(String input, int id) {
            //Toast.makeText(getContext(), "setname_button", Toast.LENGTH_SHORT).show();

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

}
