package com.example.lb_wanli_car;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by wujn on 2019/2/15.
 * Version : v1.0
 * Function: udp client 64k限制
 */
public class UdpClient {
    /**
     * single instance UdpClient
     * */
    private static UdpClient mSocketClient = null;
    private UdpClient(){}
    public static UdpClient getInstance(){
        if(mSocketClient == null){
            synchronized (UdpClient.class) {
                mSocketClient = new UdpClient();
            }
        }
        return mSocketClient;
    }


    String TAG_log = "Socket";
    private DatagramSocket mSocket;
    private DatagramPacket sendPacket;    //发送
    private DatagramPacket receivePacket; //接受
    //  private OutputStream mOutputStream;
    //  private InputStream mInputStream;

    private SocketThread mSocketThread;
    private boolean isStop = false;//thread flag

    /**
     * 128 - 数据按照最长接收，一次性
     * */

    private class SocketThread extends Thread {
        private final String ip;
        private final int port;
        public SocketThread(String ip, int port){
            this.ip = ip;
            this.port = port;
        }
        @Override
        public void run() {
            Log.d(TAG_log,"SocketThread start ");
            super.run();
            //connect ...
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                }

                InetAddress ipAddress = InetAddress.getByName(ip);
                mSocket = new DatagramSocket();
                mSocket.connect(ipAddress, port); //连接

                //设置timeout
                //mSocket.setSoTimeout(3000);
                Log.d(TAG_log,"udp connect = "+isConnect());
                if(isConnect()){
                    isStop = false;
                    uiHandler.sendEmptyMessage(1);
                }
                /* 此处这样做没什么意义不大，真正的socket未连接还是靠心跳发送，等待服务端回应比较好，一段时间内未回应，则socket未连接成功 */
                else{
                    uiHandler.sendEmptyMessage(-1);
                    Log.e(TAG_log,"SocketThread connect fail");
                    return;
                }
            } catch (IOException e) {
                uiHandler.sendEmptyMessage(-1);
                Log.e(TAG_log,"SocketThread connect io exception = "+e.getMessage());
                e.printStackTrace();                return;
            }
            Log.d(TAG_log,"SocketThread connect over ");
            //发送一次，否则不发送则收不到，不知道为啥。。。
            sendByteCmd(new byte[]{0},-1); //send once

            //read ...
            while (isConnect() && !isStop && !isInterrupted()) {
                int size;
                try {
                    byte[] preBuffer = new byte[4 * 1024];//预存buffer
                    receivePacket = new DatagramPacket(preBuffer, preBuffer.length);
                    mSocket.receive(receivePacket);
                    if (receivePacket.getData() == null) return;
                    size = receivePacket.getLength();     //此为获取后的有效长度，一次最多读64k，预存小的话可能分包
                    Log.d(TAG_log, "pre data size = "+receivePacket.getData().length + ", value data size = "+size);
                    byte[] dataBuffer = Arrays.copyOf(preBuffer, size);
                    if (size > 0) {
                        Message msg = new Message();
                        msg.what = 100;
                        Bundle bundle = new Bundle();
                        bundle.putByteArray("data",dataBuffer);
                        bundle.putInt("size",size);
                        bundle.putInt("requestCode",requestCode);
                        msg.setData(bundle);
                        uiHandler.sendMessage(msg);
                    }
                    Log.i(TAG_log, "SocketThread read listening");
                    //Thread.sleep(100);//log eof
                }
                catch (IOException e) {
                    uiHandler.sendEmptyMessage(-1);
                    Log.e(TAG_log,"SocketThread read io exception = "+e.getMessage());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    //==============================socket connect============================
    private String ip;
    private int port;    /**
     * connect socket in thread
     * Exception : android.os.NetworkOnMainThreadException
     * */
    public void connect(String ip, int port){
        this.ip = ip;
        this.port = port;
        mSocketThread = new SocketThread(ip, port);
        mSocketThread.start();
    }

    /**
     * socket is connect
     * */
    public boolean isConnect(){
        boolean flag = false;
        if (mSocket != null) {
            flag = mSocket.isConnected();
        }
        return flag;
    }
    /**
     * socket disconnect
     * */
    public void disconnect() {
        isStop = true;
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
        if (mSocketThread != null) {
            mSocketThread.interrupt();//not intime destory thread,so need a flag
        }
    }

    /**
     * send byte[] cmd
     * Exception : android.os.NetworkOnMainThreadException
     * */
    public void sendByteCmd(final byte[] mBuffer,int requestCode) {
        this.requestCode = requestCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipAddress = InetAddress.getByName(ip);
                    sendPacket = new DatagramPacket(mBuffer, mBuffer.length, ipAddress, port);
                    mSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case -1: //connect error
                    if (null != onDataReceiveListener) {
                        onDataReceiveListener.onConnectFail();
                        disconnect();
                    }
                    break;
                case 1: //connect success
                    if (null != onDataReceiveListener) {
                        onDataReceiveListener.onConnectSuccess();
                    }
                    break;
                case 100: //receive data
                    Bundle bundle = msg.getData();
                    byte[] buffer = bundle.getByteArray("data");
                    int size = bundle.getInt("size");
                    int mequestCode = bundle.getInt("requestCode");
                    if (null != onDataReceiveListener) {
                        onDataReceiveListener.onDataReceive(buffer, size, mequestCode);
                    }
                    break;
            }
        }
    };

    /**
     * socket response data listener
     * */
    private OnDataReceiveListener onDataReceiveListener = null;
    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }
    private int requestCode = -1;
    public interface OnDataReceiveListener {
        void onConnectSuccess();
        void onConnectFail();
        void onDataReceive(byte[] buffer, int size, int requestCode);
    }
}