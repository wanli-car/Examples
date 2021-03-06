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
    static public Switch mSwitch_QuickStartmode;

    static public SeekBar mSeekBar_Wheel_1;
    static public SeekBar mSeekBar_Wheel_2;
    static public SeekBar mSeekBar_Wheel_3;
    static public SeekBar mSeekBar_Wheel_4;
    static public SeekBar mSeekBar_Roll;

    static public TextView mEditText_Wheel_1;
    static public TextView mEditText_Wheel_2;
    static public TextView mEditText_Wheel_3;
    static public TextView mEditText_Wheel_4;




    static public byte RockerValue[]={1,0,0,0,0,1};
    static public byte MotorValue[]={0,0,0,0,0};
    //static public boolean isTurnmodeSwitch;
    //static public boolean isQuickStartmodeSwitch;
    private byte temp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1,
                container, false);

        WindowManager wm1 = getActivity().getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();
        Rocker=  (RockerView)view.findViewById(R.id.rockerView1);

        mSwitch_Turnmode=(Switch)view.findViewById(R.id.switch_Turnmode);
        mSwitch_QuickStartmode=(Switch)view.findViewById(R.id.switch_QuickStartmode);

        mSeekBar_Wheel_1=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_1);
        mSeekBar_Wheel_2=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_2);
        mSeekBar_Wheel_3=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_3);
        mSeekBar_Wheel_4=(SeekBar)view.findViewById(R.id.SeekBar_Wheel_4);
        mSeekBar_Roll=(SeekBar)view.findViewById(R.id.SeekBar_Roll);

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
        mSeekBar_Roll.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBar_Roll.setProgress((int)(100));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                RockerValue[2]=(byte)(progress-100);
                mecanumRun(RockerValue[1],RockerValue[0],-RockerValue[2] );
            }
        });

        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) Rocker.getLayoutParams();
        linearParams.width = width;
        linearParams.height = width;
        Rocker.setLayoutParams(linearParams);
        Rocker.setRockerChangeListener(new RockerView.RockerChangeListener() {
            @Override
            public void report(float x, float y) {
                    RockerValue[0] = (byte) (-y / Rocker.getR() * 70);
                    RockerValue[1] = (byte) (x / Rocker.getR() * 70);
                if(mSwitch_Turnmode.isChecked()){
                    Tuenmode_mecanumRun(RockerValue[0], -RockerValue[1]);
                }else{
                    mecanumRun(RockerValue[1],RockerValue[0],RockerValue[2] );
                }
            }
        });
        return view;
    }

    void Tuenmode_mecanumRun(float ySpeed, float aSpeed)
    {
        float speed1 = ySpeed  + aSpeed;
        float speed2 = ySpeed  - aSpeed;
        float speed3 = ySpeed  - aSpeed;
        float speed4 = ySpeed  + aSpeed;
        if(mSwitch_QuickStartmode.isChecked()) {
            if ((int) speed1 > 0) {
                speed1 = speed1 / 2 + 50;
            } else if ((int) speed1 < 0) {
                speed1 = speed1 / 2 - 50;
            }
            if ((int) speed2 > 0) {
                speed2 = speed2 / 2 + 50;
            } else if ((int) speed2 < 0) {
                speed2 = speed2 / 2 - 50;
            }
            if ((int) speed3 > 0) {
                speed3 = speed3 / 2 + 50;
            } else if ((int) speed3 < 0) {
                speed3 = speed3 / 2 - 50;
            }
            if ((int) speed4 > 0) {
                speed4 = speed4 / 2 + 50;
            } else if ((int) speed4 < 0) {
                speed4 = speed4 / 2 - 50;
            }
        }
        mSeekBar_Wheel_1.setProgress((int)(speed1+100));
        mSeekBar_Wheel_2.setProgress((int)(speed2+100));
        mSeekBar_Wheel_3.setProgress((int)(speed3+100));
        mSeekBar_Wheel_4.setProgress((int)(speed4+100));
    }

    void mecanumRun(float xSpeed, float ySpeed, float aSpeed)
    {
        float maxLinearSpeed=100;
        float speed1 = ySpeed - xSpeed + aSpeed;
        float speed2 = ySpeed + xSpeed - aSpeed;
        float speed3 = ySpeed - xSpeed - aSpeed;
        float speed4 = ySpeed + xSpeed + aSpeed;
        if(mSwitch_QuickStartmode.isChecked()) {
            if ((int) speed1 > 0) {
                speed1 = speed1 / 2 + 50;
            } else if ((int) speed1 < 0) {
                speed1 = speed1 / 2 - 50;
            }
            if ((int) speed2 > 0) {
                speed2 = speed2 / 2 + 50;
            } else if ((int) speed2 < 0) {
                speed2 = speed2 / 2 - 50;
            }
            if ((int) speed3 > 0) {
                speed3 = speed3 / 2 + 50;
            } else if ((int) speed3 < 0) {
                speed3 = speed3 / 2 - 50;
            }
            if ((int) speed4 > 0) {
                speed4 = speed4 / 2 + 50;
            } else if ((int) speed4 < 0) {
                speed4 = speed4 / 2 - 50;
            }
        }
        float max = speed1;
        if (max < speed2)   max = speed2;
        if (max < speed3)   max = speed3;
        if (max < speed4)   max = speed4;
        if (max > maxLinearSpeed)
        {
            speed1 = speed1 / max * maxLinearSpeed;
            speed2 = speed2 / max * maxLinearSpeed;
            speed3 = speed3 / max * maxLinearSpeed;
            speed4 = speed4 / max * maxLinearSpeed;
        }
        mSeekBar_Wheel_1.setProgress((int)(speed1+100));
        mSeekBar_Wheel_2.setProgress((int)(speed2+100));
        mSeekBar_Wheel_3.setProgress((int)(speed3+100));
        mSeekBar_Wheel_4.setProgress((int)(speed4+100));
    }
}

