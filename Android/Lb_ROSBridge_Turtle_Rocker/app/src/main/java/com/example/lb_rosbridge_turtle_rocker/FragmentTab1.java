package com.example.lb_rosbridge_turtle_rocker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class FragmentTab1 extends Fragment {

    private RockerView Rocker;

    static public Button btn_subscribe;

    static public EditText et_pose_x;
    static public EditText et_pose_y;
    static public EditText et_pose_theta;
    static public EditText et_pose_linear_velocity;
    static public EditText et_pose_angular_velocity;

    static public SeekBar mSeekBar_Wheel_1;
    static public SeekBar mSeekBar_Wheel_2;
    static public SeekBar mSeekBar_Wheel_3;

    static public TextView mEditText_Wheel_1;
    static public TextView mEditText_Wheel_2;
    static public TextView mEditText_Wheel_3;

    static public EditText EditText_Wheel_1;
    static public EditText EditText_Wheel_2;
    static public EditText EditText_Wheel_3;


   // static public double MotorValue[]={0,0,0};


    static public Switch mSwitch_Turnmode;
    static public double RockerValue[]={0,0};
    static public double CountrlValue[]={0,0,0};

    private byte temp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1,
                container, false);

        WindowManager wm1 = getActivity().getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();
        Rocker=  (RockerView)view.findViewById(R.id.rockerView1);

        mSwitch_Turnmode=(Switch)view.findViewById(R.id.switch_Turnmode);

        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) Rocker.getLayoutParams();
        linearParams.width = width;
        linearParams.height = width;

        EditText_Wheel_1=view.findViewById(R.id.EditText_Wheel_1);
        EditText_Wheel_2=view.findViewById(R.id.EditText_Wheel_2);
        EditText_Wheel_3=view.findViewById(R.id.EditText_Wheel_3);
        btn_subscribe=view.findViewById(R.id.btn_subscribe);
        et_pose_x=view.findViewById(R.id.et_pose_x);
        et_pose_y=view.findViewById(R.id.et_pose_y);
        et_pose_theta=view.findViewById(R.id.et_pose_theta);
        et_pose_linear_velocity=view.findViewById(R.id.et_pose_linear_velocity);
        et_pose_angular_velocity=view.findViewById(R.id.et_pose_angular_velocity);
        mSeekBar_Wheel_1=view.findViewById(R.id.SeekBar_Wheel_1);
        mSeekBar_Wheel_2=view.findViewById(R.id.SeekBar_Wheel_2);
        mSeekBar_Wheel_3=view.findViewById(R.id.SeekBar_Wheel_3);

        mEditText_Wheel_1=view.findViewById(R.id.EditText_Wheel_1);
        mEditText_Wheel_2=view.findViewById(R.id.EditText_Wheel_2);
        mEditText_Wheel_3=view.findViewById(R.id.EditText_Wheel_3);

        mSeekBar_Wheel_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CountrlValue[0]=(double)(progress-100)/100.0;
                mEditText_Wheel_1.setText(String.valueOf(CountrlValue[0]));
            }
        });
        mSeekBar_Wheel_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CountrlValue[1]=(double)(progress-100)/100.0;
                mEditText_Wheel_2.setText(String.valueOf(CountrlValue[1]));
            }
        });
        mSeekBar_Wheel_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CountrlValue[2]=(double)(progress-100)/100.0;
                mEditText_Wheel_3.setText(String.valueOf(CountrlValue[2]));
            }
        });
        Rocker.setLayoutParams(linearParams);
        Rocker.setRockerChangeListener(new RockerView.RockerChangeListener() {
            @Override
            public void report(float x, float y) {
                RockerValue[0] = (double) (-y / Rocker.getR() );
                RockerValue[1] = (double) (-x / Rocker.getR() );
                if(mSwitch_Turnmode.isChecked()){
                    CountrlValue[0]=RockerValue[0];
                    CountrlValue[1]=RockerValue[1];
                    CountrlValue[2]=0;
                }else{
                    if(RockerValue[0]<0){
                        CountrlValue[0]=-Math.sqrt(RockerValue[0]*RockerValue[0]+RockerValue[1]*RockerValue[1]);
                    }else {
                        CountrlValue[0]=Math.sqrt(RockerValue[0]*RockerValue[0]+RockerValue[1]*RockerValue[1]);
                    }
                    CountrlValue[1]=0;
                    CountrlValue[2]=RockerValue[1];
                }
                mEditText_Wheel_1.setText(String.valueOf(CountrlValue[0]));
                mEditText_Wheel_2.setText(String.valueOf(CountrlValue[1]));
                mEditText_Wheel_3.setText(String.valueOf(CountrlValue[2]));
            }
        });
        return view;
    }


}

