package com.example.lb_wanli_car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static com.example.lb_wanli_car.FragmentTab4.sv_receive;





public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
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
    public Toolbar toolbar;
    public static  boolean Remote_Flag=false;
    static public Handler handler = new Handler();
    public static TextView topdisplay;
    private boolean Connect_Flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明化导航栏
        }
        //
        topdisplay=(TextView)findViewById(R.id.top_display);
        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        UdpClient.getInstance().setOnDataReceiveListener(dataReceiveListener);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_clear:
                FragmentTab4.et_receive.setText("");
                break;
            case R.id.btn_send:
                if (UdpClient.getInstance().isConnect()) {
                    byte[] data=FragmentTab4.et_send.getText().toString().getBytes();
                    String str = new String(data);
                    Log.i("TAG_log",str);
                    UdpClient.getInstance().sendByteCmd(data,1001);
                } else {
                    Toast.makeText(MainActivity.this,"尚未连接，请连接Socket",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_connect:
                String ip = FragmentTab4.et_ip.getText().toString();
                String port = FragmentTab4.et_port.getText().toString();

                if(TextUtils.isEmpty(ip)){
                    Toast.makeText(MainActivity.this,"IP地址为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(port)){
                    Toast.makeText(MainActivity.this,"端口号为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                UdpClient.getInstance().connect(ip, Integer.parseInt(port));
                break;
            case R.id.btn_disconnect:
                UdpClient.getInstance().disconnect();
                FragmentTab4.tv_state.setText("未连接");
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
    public Runnable runnable = new Runnable() {

        int temp;
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 100);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (UdpClient.getInstance().isConnect()) {
                            byte[] data=FragmentTab1.MotorValue;
                            //String str = new String(data);
                            Log.i("TAG_log",bytes2HexString(data));
                            UdpClient.getInstance().sendByteCmd(data,1001);
                        }
                       // Log.e("test", String.valueOf(FragmentTab1.RockerValue[1]));

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
        messageImage = (ImageView) findViewById(R.id.message_image);
        contactsImage = (ImageView) findViewById(R.id.contacts_image);
        newsImage = (ImageView) findViewById(R.id.news_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        messageText = (TextView) findViewById(R.id.message_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        newsText = (TextView) findViewById(R.id.news_text);
        settingText = (TextView) findViewById(R.id.setting_text);
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
    private UdpClient.OnDataReceiveListener dataReceiveListener = new UdpClient.OnDataReceiveListener() {
        @Override
        public void onConnectSuccess() {
            Log.i("TAG_log","onDataReceive connect success");
            FragmentTab4.tv_state.setText("已连接");
            topdisplay.setText("连接成功");
            //Connect_Flag=true;
            handler.postDelayed(runnable, 100);//开启周期性的定时
        }
        @Override
        public void onConnectFail() {
            Log.e("TAG_log","onDataReceive connect fail");
            FragmentTab4.tv_state.setText("未连接");
            topdisplay.setText("断开连接");
            //Connect_Flag=false;
            handler.removeCallbacks(runnable);//关闭定时
        }
        @Override
        public void onDataReceive(byte[] buffer, int size, int requestCode) {
            //获取有效长度的数据
            byte[] data = new byte[size];
            System.arraycopy(buffer, 0, data, 0, size);
            String oxValue = new String(data);
            Log.i("TAG_log","onDataReceive requestCode = "+requestCode + ", content = "+oxValue);
            if(!FragmentTab4.switch_Turnoff_Rec.isChecked()){
                FragmentTab4.et_receive.append(oxValue + "\n");
                FragmentTab4.sv_receive.fullScroll(View.FOCUS_DOWN);
            }
        }
    };

    @Override
    protected void onDestroy() {
        UdpClient.getInstance().disconnect();
        super.onDestroy();
        handler.removeCallbacks(runnable);//关闭定时
    }
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }
}