package com.example.lb_socket_udp_simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private Button btn_listen;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et= (EditText) findViewById (R.id.edit_text);
        btn_listen= (Button) findViewById (R.id.button);
        btn_listen.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                et.setText ("开始发送数据...");
                new SendThread().start();

            }
        });
    }
}
class SendThread extends Thread {
    public void run(){
        try {
            //首先创建一个 DatagramSocket 对象
            DatagramSocket socket=new DatagramSocket(1235);
            //创建一个InetAddree,自己测试的时候要设置成自己的IP地址
            InetAddress serverAddress= InetAddress.getByName ("192.168.31.34");
            while (true) {
                String str="Hi, lb8820265!";
                byte data []=str.getBytes ();
                //创建一个 DatagramPacket 对象，并指定要将这个数据包发送到网络当中的哪个地址以及端口号
                DatagramPacket packet=new DatagramPacket(data,data.length,serverAddress,1234);
                //调用 socket 对象的 send 方法，发送数据
                socket.send (packet) ;
                Log.d ("server", "sending...") ;
                Thread.sleep (1000) ;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}