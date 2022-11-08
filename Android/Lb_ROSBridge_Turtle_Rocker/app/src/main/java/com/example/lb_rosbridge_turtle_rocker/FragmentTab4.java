package com.example.lb_rosbridge_turtle_rocker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


public class FragmentTab4 extends Fragment {
    static public EditText et_ip;
    static public EditText et_port;

    static public TextView tv_state;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4,
                container, false);


        et_ip=view.findViewById(R.id.et_ip);
        et_port=view.findViewById(R.id.et_port);

        tv_state=view.findViewById(R.id.tv_state);



        return view;
    }



}

