package com.example.lb_rosbridge_turtle_rocker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jilk.ros.ROSClient;
import com.jilk.ros.rosbridge.ROSBridgeClient;
import com.jilk.ros.rosbridge.implementation.PublishEvent;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    ROSBridgeClient client;
    String msg;
    private FragmentTab1 messageFragment;
    private FragmentTab2 contactsFragment;
    private FragmentTab3 newsFragment;
    private FragmentTab4 settingFragment;
    private View messageLayout;
    private View contactsLayout;
    private View newsLayout;
    private View settingLayout;
    private ImageView messageImage;
    private ImageView contactsImage;
    private ImageView newsImage;
    private ImageView settingImage;
    private TextView messageText;
    private TextView contactsText;
    private TextView newsText;
    private TextView settingText;
    private FragmentManager fragmentManager;

    public static TextView topdisplay;

    double Pose_Data[]={0,0,0,0,0};

    static public Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明化导航栏
        }
        //
        topdisplay=findViewById(R.id.top_display);
        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }
    public Runnable runnable = new Runnable() {

        int temp;
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 100);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SendDataToRos(FragmentTab1.CountrlValue[0],FragmentTab1.CountrlValue[1],FragmentTab1.CountrlValue[2]);
                        FragmentTab1.et_pose_x.setText(String.valueOf(Pose_Data[0]));
                        FragmentTab1.et_pose_y.setText(String.valueOf(Pose_Data[1]));
                        FragmentTab1.et_pose_theta.setText(String.valueOf(Pose_Data[2]));
                        FragmentTab1.et_pose_linear_velocity.setText(String.valueOf(Pose_Data[3]));
                        FragmentTab1.et_pose_angular_velocity.setText(String.valueOf(Pose_Data[4]));
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    };
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

            }
            return true;
        }
    };

    private void initViews() {
        messageLayout = findViewById(R.id.message_layout);
        contactsLayout = findViewById(R.id.contacts_layout);
        newsLayout = findViewById(R.id.news_layout);
        settingLayout = findViewById(R.id.setting_layout);
        messageImage =  findViewById(R.id.message_image);
        contactsImage =  findViewById(R.id.contacts_image);
        newsImage =  findViewById(R.id.news_image);
        settingImage =  findViewById(R.id.setting_image);
        messageText =  findViewById(R.id.message_text);
        contactsText =  findViewById(R.id.contacts_text);
        newsText =  findViewById(R.id.news_text);
        settingText =  findViewById(R.id.setting_text);
        messageLayout.setOnClickListener(this);
        contactsLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }


    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:

                messageImage.setImageResource(R.drawable.message_selected);
                messageText.setTextColor(Color.WHITE);
                if (messageFragment == null) {

                    messageFragment = new FragmentTab1();
                    transaction.add(R.id.content, messageFragment);
                } else {

                    transaction.show(messageFragment);
                }
                break;
            case 1:


                contactsImage.setImageResource(R.drawable.contacts_selected);
                contactsText.setTextColor(Color.WHITE);
                if (contactsFragment == null) {

                    contactsFragment = new FragmentTab2();
                    transaction.add(R.id.content, contactsFragment);
                } else {

                    transaction.show(contactsFragment);
                }
                break;
            case 2:

                newsImage.setImageResource(R.drawable.news_selected);
                newsText.setTextColor(Color.WHITE);
                if (newsFragment == null) {

                    newsFragment = new FragmentTab3();
                    transaction.add(R.id.content, newsFragment);
                } else {

                    transaction.show(newsFragment);
                }
                break;
            case 3:
            default:

                settingImage.setImageResource(R.drawable.setting_selected);
                settingText.setTextColor(Color.WHITE);
                if (settingFragment == null) {

                    settingFragment = new FragmentTab4();
                    transaction.add(R.id.content, settingFragment);
                } else {

                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }
    private void clearSelection() {
        messageImage.setImageResource(R.drawable.message_unselected);
        messageText.setTextColor(Color.parseColor("#82858b"));
        contactsImage.setImageResource(R.drawable.contacts_unselected);
        contactsText.setTextColor(Color.parseColor("#82858b"));
        newsImage.setImageResource(R.drawable.news_unselected);
        newsText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.setting_unselected);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (contactsFragment != null) {
            transaction.hide(contactsFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                String ip = FragmentTab4.et_ip.getText().toString();
                String port = FragmentTab4.et_port.getText().toString();
                connect(ip, port);
                break;

            case R.id.btn_disconnect:
                client.disconnect();
                break;
            //case R.id.btn_publish:
            //    SendDataToRos(FragmentTab1.MotorValue[0],FragmentTab1.MotorValue[1],FragmentTab1.MotorValue[2]);
                //showTip("发布");
            //   break;
            case R.id.btn_subscribe:
                if(FragmentTab1.btn_subscribe.getText().equals("订阅位置")){
                    msg = "{\"op\":\"subscribe\",\"topic\":\"/turtle1/pose\"}";
                    client.send(msg);
                    FragmentTab1.btn_subscribe.setText("取消订阅");
                    showTip("订阅位置");
                }else{
                    msg = "{\"op\":\"unsubscribe\",\"topic\":\"/turtle1/pose\"}";
                    client.send(msg);
                    FragmentTab1.btn_subscribe.setText("订阅位置");
                    showTip("取消订阅");
                }

                break;
            case R.id.message_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.contacts_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.news_layout:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }
    private void connect(String ip, String port) {
        client = new ROSBridgeClient("ws://" + ip + ":" + port);
        boolean conneSucc = client.connect(new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                client.setDebug(true);
                ((RCApplication)getApplication()).setRosClient(client);
                showTip("连接成功");
                handler.postDelayed(runnable, 100);//开启周期性的定时
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTab4.tv_state.setText("已连接");
                        topdisplay.setText("设备已连接");
                    }
                });
                Log.d(TAG,"Connect ROS success");
                //startActivity(new Intent(MainActivity.this,NodesActivity.class));
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                showTip("断开成功");
                handler.removeCallbacks(runnable);//关闭定时
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTab4.tv_state.setText("未连接");
                        topdisplay.setText("设备未连接");
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

                Pose_Data[0]=(double)jsonObj.get("x");
                Pose_Data[1] = (double)jsonObj.get("y");
                Pose_Data[2]= (double)jsonObj.get("theta");
                Pose_Data[3] = (double)jsonObj.get("linear_velocity");
                Pose_Data[4]= (double)jsonObj.get("angular_velocity");

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        handler.removeCallbacks(runnable);//关闭定时
    }
}