package com.example.lb_wanli_car;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;


public class FragmentTab4 extends Fragment {

    static public EditText et_ip;
    static public EditText et_port;
    static public EditText et_send;
    static public EditText et_receive;
    static public TextView tv_state;
    static public ScrollView sv_receive;
    static public Switch switch_Turnoff_Rec;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4,
                container, false);

        et_ip= (EditText) view.findViewById (R.id.et_ip);
        et_port= (EditText) view.findViewById (R.id.et_port);
        et_send=(EditText) view.findViewById (R.id.et_send);
        tv_state= (TextView) view.findViewById (R.id.tv_state);
        et_receive= (EditText) view.findViewById (R.id.et_receive);
        sv_receive= (ScrollView) view.findViewById (R.id.sv_receive);
        switch_Turnoff_Rec=(Switch)view.findViewById (R.id.switch_Turnoff_Rec);
        return view;
    }



}

