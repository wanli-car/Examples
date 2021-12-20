#include <stdio.h>
#include <wiringPi.h>

#include <iostream>
#include "Emakefun_MotorShield.h"
#define SPEED_INTEGRAL_MAX  1

float Speed_Kp=255,Speed_Ki=150;
float Set_Speet=0.5;
int A_pin=21;
int B_pin=22;

float Encoder_Integral=0;		
long Cnt;
long Cha;
long now,pre_time;
float Speed=0;
float Set_PWM_float=0;
uint8_t Set_PWM=0; 
Emakefun_DCMotor *DCmotor1 ;
//电机速度PI控制函数
float Speed_PI(float Now_Speed,float Set_Speet)
{
	static float fP;
	fP=Set_Speet-Now_Speed;
	Encoder_Integral+=fP;
	if(Encoder_Integral>SPEED_INTEGRAL_MAX)  	Encoder_Integral=SPEED_INTEGRAL_MAX;           
	return fP*Speed_Kp+Encoder_Integral*Speed_Ki;  
}
//外部中断回调函数，用来计数电机速度
void Interrupt (void) {
   Cnt++;
}
//定时器线程函数
PI_THREAD (timer)
{
  for (;;)
  {
	now=micros();
  	Cha=(now-pre_time);
	Speed=(float)Cnt/Cha*50;
	Cnt=0;
	pre_time= now;
	Set_PWM_float=Speed_PI(Speed, Set_Speet);
	if(Set_PWM_float>255)Set_PWM_float=255;
	if(Set_PWM_float<0)Set_PWM_float=0;
	Set_PWM=(uint8_t)Set_PWM_float;
	DCmotor1->setSpeed(Set_PWM);
 	printf ("%ld,%f,%f,%f\n",Cha,Speed,Set_PWM_float,Encoder_Integral);
	delay(100);
  }
}
int main () {
	Emakefun_MotorShield Pwm = Emakefun_MotorShield();
	Pwm.begin(50);
	 DCmotor1 = Pwm.getMotor(1);
	 DCmotor1->setSpeed(0);
	 DCmotor1->run(FORWARD);
  	wiringPiSetup () ;
  	wiringPiISR (A_pin, INT_EDGE_BOTH, &Interrupt) ;
  	wiringPiISR (B_pin, INT_EDGE_BOTH, &Interrupt) ;
  	piThreadCreate (timer) ;
	while(1) {
		delay(1000);
	}
}
