#ifndef _CONTROL_H
#define _CONTROL_H



#include <stdio.h>
#include <iostream>
#include <wiringPi.h>
#include "Emakefun_MotorShield.h"
#define SPEED_INTEGRAL_MAX  1
struct Motor
{
    int Pin_A;//广电编码A引脚
    int Pin_B;//广电编码B引脚
    float Speed_Now;//当前速度
    float Speed_Set;//设置的速度
    uint8_t Direction;//设置的方向
    int Encoder_Cnt;//编码器计数
    float Encoder_Integral;//编码器积分
    Emakefun_DCMotor *Handle;//电机句柄
    float Motor_out_float;//计算出来的电机输出值
    uint8_t Motor_out;//转化为驱动输入的字节格式
   
};
void Encoder_Init();
void Motor_Init();
void Do_Motor(long Time_Period);
#endif