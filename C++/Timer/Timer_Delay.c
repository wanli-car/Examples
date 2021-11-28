/*
 * timer_test2.c:
 *	采用线程和延时函数来实现定时器功能，创造两个线程实现两个定时器功能
 *	际定时都比延时时间长
 *
 */

#include <stdio.h>
#include <wiringPi.h>

long tim,tim2;
long now,now2;
long pre_time,pre_time2;

#define INTERVAL 10000 //间隔时间，单位是us

PI_THREAD (timer1)
{
  for (;;)
  {
	now=micros();
	delay(10);
		printf ("1:%ld,%ld\n",now,now-pre_time);
		pre_time=now;
	
  }
}

PI_THREAD (timer2)
{
  for (;;)
  {
	now2=micros();
	delay(10);
		tim2 += INTERVAL;
		printf ("2:%ld,%ld\n",now2,now2-pre_time2);
		pre_time2=now2;
	
  }
}

int main (void)
{
  wiringPiSetup () ;
  tim = micros();
  piThreadCreate (timer1) ;
  piThreadCreate (timer2) ;
  for (;;)
  {
  }
  return 0 ;
}
