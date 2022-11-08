package com.example.lb_rosbridge_turtle_rocker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;


/**
 * Created by acer on 2016/3/17.
 */
public class RockerView extends View {
    //�̶�ҡ�˱���Բ�ε�X,Y�����Լ��뾶
    private float mRockerBg_X;
    private float mRockerBg_Y;
    private float mRockerBg_R;
    //ҡ�˵�X,Y�����Լ�ҡ�˵İ뾶
    private float mRockerBtn_X;
    private float mRockerBtn_Y;
    private float mRockerBtn_R;
    private Bitmap mBmpRockerBg;
    private Bitmap mBmpRockerBtn;
    private PointF mCenterPoint;
    //���µ�ȷ����־λ
    private boolean mRockerSure;
    public RockerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        // ��ȡbitmap
        mBmpRockerBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocker_bg);
        mBmpRockerBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocker_btn);
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            // ���ø÷���ʱ���Ի�ȡviewʵ�ʵĿ�getWidth()�͸�getHeight()
            @Override
            public boolean onPreDraw() {
                // TODO Auto-generated method stub
                getViewTreeObserver().removeOnPreDrawListener(this);
                Log.e("RockerView", getWidth() + "/" + getHeight());
                mCenterPoint = new PointF(getWidth() / 2, getHeight() / 2);
                mRockerBg_X = mCenterPoint.x;
                mRockerBg_Y = mCenterPoint.y;
                mRockerBtn_X = mCenterPoint.x;
                mRockerBtn_Y = mCenterPoint.y;
                //ȷ��View�ı߿��С����ť�ͱ����ı���
                float tmp_Bg = mBmpRockerBg.getWidth() / (float)(mBmpRockerBg.getWidth()*2 + mBmpRockerBtn.getWidth());
                mRockerBg_R = tmp_Bg * getWidth() / 2;
                float tmp_Btn =  mBmpRockerBtn.getWidth()/ (float)(mBmpRockerBg.getWidth()*2 + mBmpRockerBtn.getWidth());
                mRockerBtn_R =tmp_Btn* getWidth() / 2;
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(true){
                    //ϵͳ����onDraw����ˢ�»���
                    RockerView.this.postInvalidate();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawBitmap(mBmpRockerBg, null,
                new Rect((int) (mRockerBg_X - mRockerBg_R),
                        (int) (mRockerBg_Y - mRockerBg_R),
                        (int) (mRockerBg_X + mRockerBg_R),
                        (int) (mRockerBg_Y + mRockerBg_R)),
                null);
        canvas.drawBitmap(mBmpRockerBtn, null,
                new Rect((int) (mRockerBtn_X - mRockerBtn_R),
                        (int) (mRockerBtn_Y - mRockerBtn_R),
                        (int) (mRockerBtn_X + mRockerBtn_R),
                        (int) (mRockerBtn_Y + mRockerBtn_R)),
                null);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //���㴥��Ļ����ԲȦ��
            if (Math.sqrt(Math.pow((mRockerBg_X - (int) event.getX()), 2) + Math.pow((mRockerBg_Y - (int) event.getY()), 2)) <= mRockerBg_R) {
                mRockerBtn_X = (int) event.getX();
                mRockerBtn_Y = (int) event.getY();
                mRockerBg_X = (int) event.getX();
                mRockerBg_Y = (int) event.getY();
                mRockerSure=true;
            }
        }
        else if ( event.getAction() == MotionEvent.ACTION_MOVE&&mRockerSure) {
            //�����㻬����֮ǰ������ԲȦ��
            // �����������ڻ��Χ��
            if (Math.sqrt(Math.pow((mRockerBg_X - (int) event.getX()), 2) + Math.pow((mRockerBg_Y - (int) event.getY()), 2)) >= mRockerBg_R) {
                //�õ�ҡ���봥�������γɵĽǶ�
                double tempRad = getRad(mRockerBg_X, mRockerBg_Y, event.getX(), event.getY());
                //��֤�ڲ�СԲ�˶��ĳ�������
                getXY(mRockerBg_X, mRockerBg_Y, mRockerBg_R, tempRad);
            } else {//���С�����ĵ�С�ڻ�����������û��������ƶ�����
                mRockerBtn_X = (int) event.getX();
                mRockerBtn_Y = (int) event.getY();
            }
            if(mRockerChangeListener != null) {
            /*
            //������Ҫ�޶�ʮ�ַ�����Ҫ����ע����λ�
            if(Math.abs(mRockerBtn_X - mRockerBg_X)>Math.abs(mRockerBtn_Y - mRockerBg_Y)){
               mRockerBtn_Y = mRockerBg_Y;
            }
            else{
               mRockerBtn_X = mRockerBg_X;
            }
            */
                mRockerChangeListener.report(mRockerBtn_X - mRockerBg_X, mRockerBtn_Y -mRockerBg_Y);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //���ͷŰ���ʱҡ��Ҫ�ָ�ҡ�˵�λ��Ϊ��ʼλ��
            mRockerBtn_X = mCenterPoint.x;
            mRockerBtn_Y = mCenterPoint.y;
            mRockerBg_X = mCenterPoint.x;
            mRockerBg_Y = mCenterPoint.y;
            mRockerSure=false;
            if(mRockerChangeListener != null) {
                mRockerChangeListener.report(0, 0);
            }
        }
        return true;
    }
    public double getRad(float px1, float py1, float px2, float py2) {
        //�õ�����X�ľ���
        float x = px2 - px1;
        //�õ�����Y�ľ���
        float y = py1 - py2;
        //���б�߳�
        float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        //�õ�����Ƕȵ�����ֵ��ͨ�����Ǻ����еĶ��� ���ڱ�/б��=�Ƕ�����ֵ��
        float cosAngle = x / xie;
        //ͨ�������Ҷ����ȡ����ǶȵĻ���
        float rad = (float) Math.acos(cosAngle);
        //ע�⣺��������λ��Y����<ҡ�˵�Y��������Ҫȡ��ֵ-0~-180
        if (py2 < py1) {
            rad = -rad;
        }
        return rad;
    }
    /**
     *
     * @param R  Բ���˶�����ת��
     * @param centerX ��ת��X
     * @param centerY ��ת��Y
     * @param rad ��ת�Ļ���
     */
    public void getXY(float centerX, float centerY, float R, double rad) {
        //��ȡԲ���˶���X����
        mRockerBtn_X = (float) (R * Math.cos(rad)) + centerX;
        //��ȡԲ���˶���Y����
        mRockerBtn_Y = (float) (R * Math.sin(rad)) + centerY;
    }
    public float getR(){
        return mRockerBg_R;
    }
    RockerChangeListener mRockerChangeListener = null;
    public void setRockerChangeListener(RockerChangeListener rockerChangeListener) {
        mRockerChangeListener = rockerChangeListener;
    }
    public interface RockerChangeListener {
        public void report(float x, float y);
    }
}

