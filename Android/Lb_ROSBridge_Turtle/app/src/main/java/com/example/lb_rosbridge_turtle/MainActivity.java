package com.example.lb_rosbridge_turtle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jilk.ros.ROSClient;
import com.jilk.ros.rosbridge.ROSBridgeClient;
import com.jilk.ros.rosbridge.implementation.PublishEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    ROSBridgeClient client;
    EditText et_ip;
    EditText et_port;
    EditText et_pose_x;
    EditText et_pose_y;
    EditText et_pose_theta;
    EditText et_pose_linear_velocity;
    EditText et_pose_angular_velocity;

     SeekBar mSeekBar_Wheel_1;
     SeekBar mSeekBar_Wheel_2;
     SeekBar mSeekBar_Wheel_3;

     TextView mEditText_Wheel_1;
     TextView mEditText_Wheel_2;
     TextView mEditText_Wheel_3;
     double MotorValue[]={0,0,0};

    TextView tv_state;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        et_ip=findViewById(R.id.et_ip);
        et_port=findViewById(R.id.et_port);
        et_pose_x=findViewById(R.id.et_pose_x);
        et_pose_y=findViewById(R.id.et_pose_y);
        et_pose_theta=findViewById(R.id.et_pose_theta);
        et_pose_linear_velocity=findViewById(R.id.et_pose_linear_velocity);
        et_pose_angular_velocity=findViewById(R.id.et_pose_angular_velocity);
        tv_state=findViewById(R.id.tv_state);

        mSeekBar_Wheel_1=findViewById(R.id.SeekBar_Wheel_1);
        mSeekBar_Wheel_2=findViewById(R.id.SeekBar_Wheel_2);
        mSeekBar_Wheel_3=findViewById(R.id.SeekBar_Wheel_3);

        mEditText_Wheel_1=findViewById(R.id.EditText_Wheel_1);
        mEditText_Wheel_2=findViewById(R.id.EditText_Wheel_2);
        mEditText_Wheel_3=findViewById(R.id.EditText_Wheel_3);

        mSeekBar_Wheel_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MotorValue[0]=(double)(progress-100)/100.0;
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
                MotorValue[1]=(double)(progress-100)/100.0;
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
                MotorValue[2]=(double)(progress-100)/100.0;
                mEditText_Wheel_3.setText(String.valueOf(MotorValue[2]));
            }
        });
    }


    private void connect(String ip, String port) {
        client = new ROSBridgeClient("ws://" + ip + ":" + port);
        boolean conneSucc = client.connect(new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                client.setDebug(true);
                ((RCApplication)getApplication()).setRosClient(client);
                showTip("连接成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_state.setText("已连接");
                    }
                });
                Log.d(TAG,"Connect ROS success");
                //startActivity(new Intent(MainActivity.this,NodesActivity.class));
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                showTip("断开成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_state.setText("未连接");
                    }
                });
                Log.d(TAG,"ROS disconnect");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                showTip("ROS communication error");
                Log.d(TAG,"ROS communication error");
            }
        });
    }
    private void showTip(final String tip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, tip,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SendDataToRos(double x,double y,double z) {
                 msg = "{\"op\":\"publish\",\"topic\":\"/turtle1/cmd_vel\",\"msg\":{\"linear\":{\"x\":" + x + ",\"y\":" +
                        y + ",\"z\":0},\"angular\":{\"x\":0,\"y\":0,\"z\":" + z + "}}}";
        Log.d(TAG,msg);
        client.send(msg);
    }

    public void onEvent(final PublishEvent event)  {
        if ("/turtle1/pose".equals(event.name)) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(event.msg);
                double pose_x = (double)jsonObj.get("x");
                double pose_y = (double)jsonObj.get("y");
                double pose_theta = (double)jsonObj.get("theta");
                double pose_linear_velocity = (double)jsonObj.get("linear_velocity");
                double pose_angular_velocity = (double)jsonObj.get("angular_velocity");

                et_pose_x.setText(String.valueOf(pose_x));
                et_pose_y.setText(String.valueOf(pose_y));
                et_pose_theta.setText(String.valueOf(pose_theta));
                et_pose_linear_velocity.setText(String.valueOf(pose_linear_velocity));
                et_pose_angular_velocity.setText(String.valueOf(pose_angular_velocity));

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                String ip = et_ip.getText().toString();
                String port = et_port.getText().toString();
                connect(ip, port);
                break;

            case R.id.btn_disconnect:
                client.disconnect();
                break;
            case R.id.btn_publish:

                SendDataToRos(MotorValue[0],MotorValue[1],MotorValue[2]);
                //showTip("发布");
                break;
            case R.id.btn_subscribe:
                 msg = "{\"op\":\"subscribe\",\"topic\":\"/turtle1/pose\"}";
                client.send(msg);
                showTip("订阅位置");
                break;
            case R.id.btn_unsubscribe:
                 msg = "{\"op\":\"unsubscribe\",\"topic\":\"/turtle1/pose\"}";
                client.send(msg);
                showTip("取消订阅");
                break;
            default:
                break;
        }
    }
}