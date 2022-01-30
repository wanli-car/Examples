
#include <wiringPi.h>
#include "Control.h"
#include "Socket_UDP.h"
long Cha;
long now,pre_time;
//定时器线程函数
PI_THREAD (timer)
{
  for (;;)
  {
	now=micros();
  	Cha=(now-pre_time);
	//Do_Motor(Cha);
	pre_time= now;
	delay(100);
  }
}
PI_THREAD (Socket)
{
  for (;;)
  {
	Socket_Task();
  }
}
int main () {
	Motor_Init();
    Encoder_Init();
	Socket_Init();
	piThreadCreate (timer) ;
	piThreadCreate (Socket) ;
	while(1) {
		//delay(1000);
	}
}