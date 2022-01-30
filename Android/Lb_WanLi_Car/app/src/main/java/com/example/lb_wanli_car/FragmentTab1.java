package com.example.lb_wanli_car;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class FragmentTab1 extends Fragment {

    private RockerView Rocker;

    static public Switch mSwitch_Turnmode;

    static public SeekBar mSeekBar_Wheel_1;
    static public SeekBar mSeekBar_Wheel_2;
    static public SeekBar mSeekBar_Wheel_3;
    static public SeekBar mSeekBar_Wheel_4;

    static public TextView mEditText_Wheel_1;
    static public TextView mEditText_Wheel_2;
    static public TextView mEditText_Wheel_3;
    static public TextView mEditText_Wheel_4;

    static public byte RockerValue[]={1,0,0,0,0,1};
    static public byte MotorValue[]={0,0,0,0,0};
    static public boolean isTurnmodeSwitch;
    private byte temp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1,
                container, false);

        WindowManager wm1 = getActivity().getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();
        Rocker=  (RockerView)view.findViewById(R.id.rockerView1);

        mSwitch_Turnmode=(Switch)view.findViewById(R.id.switch_Turnmode);

        mSeekBar_Wheel_1=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_1);
        mSeekBar_Wheel_2=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_2);
        mSeekBar_Wheel_3=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_3);
        mSeekBar_Wheel_4=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_4);
        mEditText_Wheel_1=(EditText)view.findViewById(R.id.EditText_Wheel_1);
        mEditText_Wheel_2=(EditText)view.findViewById(R.id.EditText_Wheel_2);
        mEditText_Wheel_3=(EditText)view.findViewById(R.id.EditText_Wheel_3);
        mEditText_Wheel_4=(EditText)view.findViewById(R.id.EditText_Wheel_4);

        mSeekBar_Wheel_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {}
              @Override
             public void onStartTrackingTouch(SeekBar seekBar) {}
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 MotorValue[0]=(byte)(progress-100);
                 mEditText_Wheel_1.setText(String.valueOf(MotorValue[0]));
             }
         });
        mSeekBar_Wheel_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MotorValue[1]=(byte)(progress-100);
                mEditText_Wheel_2.setText(String.valueOf(MotorValue[1]));
            }
        });
        mSeekBar_Wheel_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MotorValue[2]=(byte)(progress-100);
                mEditText_Wheel_3.setText(String.valueOf(MotorValue[2]));
            }
        });
        mSeekBar_Wheel_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MotorValue[3]=(byte)(progress-100);
                mEditText_Wheel_4.setText(String.valueOf(MotorValue[3]));
            }
        });


        mSwitch_Turnmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   isTurnmodeSwitch=true;
                //    oriturnmodeSwitch.setEnabled(true);


                }
                else {
                  isTurnmodeSwitch=false;
                   // oriturnmodeSwitch.setEnabled(false);

                }
            }
        });
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) Rocker.getLayoutParams();
        linearParams.width = width;
        linearParams.height = width;
        Rocker.setLayoutParams(linearParams);
        Rocker.setRockerChangeListener(new RockerView.RockerChangeListener() {
            @Override
            public void report(float x, float y) {
                RockerValue[0] = (byte) (y / Rocker.getR() * 100);
                RockerValue[1] = (byte) (x / Rocker.getR() * 100);
                // System.out.println(RockerValue[0]);
            }
        });
        return view;
    }
}

