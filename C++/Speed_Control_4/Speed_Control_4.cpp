
#include <wiringPi.h>

#include "Control.h"

long Cha;
long now,pre_time;


//定时器线程函数
PI_THREAD (timer)
{
  for (;;)
  {
	now=micros();
  	Cha=(now-pre_time);
	Do_Motor(Cha);
	pre_time= now;
	
 	//printf ("%ld,%f,%f,%f\n",Cha,Speed,Set_PWM_float,Encoder_Integral);
	delay(50);
  }
}
int main () {
	Motor_Init();
    Encoder_Init();
	
	piThreadCreate (timer) ;
  	
	while(1) {
		delay(1000);
	}
}