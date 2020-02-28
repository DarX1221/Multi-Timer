package com.example.multitimer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private EditText editText;
    private Button setNameButton;




    public interface FragmentNameListener {
        void onInputNameSent(String input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        editText = view.findViewById(R.id.editText);
        setNameButton = view.findViewById(R.id.setname_button);
        setNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                listener.onInputNameSent(input);
            }
        });
        return view;
    }

    private FragmentNameListener listener = new FragmentNameListener() {
        @Override
        public void onInputNameSent(String input) {
            Toast.makeText(getContext(), "setname_button", Toast.LENGTH_SHORT).show();
            String name = editText.getText().toString();
            listener.onInputNameSent(name);
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
    public void onDetach(){
        super.onDetach();
        listener = null;
    }
}
