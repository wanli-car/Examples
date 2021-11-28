
#include <stdio.h>
#include <wiringPi.h>

long now;
long pre_time;

void myInterrupt (void) {
	 
	 now=micros();
	 printf ("%ld,%ld\n",now,now-pre_time);
	 pre_time=now;
	 
}

int main (void)
{
  wiringPiSetup () ;
  pinMode (1, PWM_OUTPUT) ;
  pwmSetMode(PWM_MODE_MS);
  pwmSetClock (540);
  pwmSetRange (1000);
  pwmWrite (1, 500) ;
  wiringPiISR (0, INT_EDGE_FALLING, &myInterrupt) ;

	for(;;){
	
	}
  return 0 ;
}
