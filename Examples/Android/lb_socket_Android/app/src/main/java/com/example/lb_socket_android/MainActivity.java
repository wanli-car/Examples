package com.example.lb_socket_android;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText et_ip;
    private EditText et_port;
    private EditText et_send;
    private EditText et_receive;
    private TextView tv_state;
    private ScrollView sv_receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_ip= (EditText) findViewById (R.id.et_ip);
        et_port= (EditText) findViewById (R.id.et_port);
        et_send=(EditText) findViewById (R.id.et_send);
        tv_state= (TextView) findViewById (R.id.tv_state);
        et_receive= (EditText) findViewById (R.id.et_receive);
        sv_receive= (ScrollView) findViewById (R.id.sv_receive);

        UdpClient.getInstance().setOnDataReceiveListener(dataReceiveListener);
    }
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_clear:
                et_receive.setText("");
                break;
            case R.id.btn_send:
                if (UdpClient.getInstance().isConnect()) {
                    byte[] data=et_send.getText().toString().getBytes();
                    String str = new String(data);
                    Log.i("TAG_log",str);
                    UdpClient.getInstance().sendByteCmd(data,1001);
                } else {
                    Toast.makeText(MainActivity.this,"尚未连接，请连接Socket",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_connect:
                String ip = et_ip.getText().toString();
                String port = et_port.getText().toString();

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
                tv_state.setText("未连接");
                break;
            default:
                break;
        }
    }
    private UdpClient.OnDataReceiveListener dataReceiveListener = new UdpClient.OnDataReceiveListener() {
        @Override
        public void onConnectSuccess() {
            Log.i("TAG_log","onDataReceive connect success");
            tv_state.setText("已连接");
        }
        @Override
        public void onConnectFail() {
            Log.e("TAG_log","onDataReceive connect fail");
            tv_state.setText("未连接");
        }
        @Override
        public void onDataReceive(byte[] buffer, int size, int requestCode) {
            //获取有效长度的数据
            byte[] data = new byte[size];
            System.arraycopy(buffer, 0, data, 0, size);
            String oxValue = new String(data);
            Log.i("TAG_log","onDataReceive requestCode = "+requestCode + ", content = "+oxValue);
            et_receive.append(oxValue + "\n");
            sv_receive.fullScroll(View.FOCUS_DOWN);
        }
    };

    @Override
    protected void onDestroy() {
        UdpClient.getInstance().disconnect();
        super.onDestroy();
    }
}

