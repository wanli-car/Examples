
#include <stdio.h>
#include <wiringPi.h>
long Cnt;
int A_pin=21;
int B_pin=22;

void AInterrupt (void) {
  if(digitalRead (A_pin) ==1){//上升沿
    if(digitalRead (B_pin) ==1){//高电平
      Cnt++;
      printf ("+");
    }else{
      Cnt--;
       printf ("-");
    }
  }else{
    if(digitalRead (B_pin) ==1){//高电平
      Cnt--;
       printf ("-");
    }else{
      Cnt++;
      printf ("+");
    }
  }
  //printf ("%ld\n",Cnt);
}

void BInterrupt (void) {
  if(digitalRead (B_pin) ==1){//上升沿
    if(digitalRead (A_pin) ==1){//高电平
      Cnt--;
       printf ("-");
    }else{
      Cnt++;
      printf ("+");
    }
  }else{
    if(digitalRead (A_pin) ==1){//高电平
      Cnt++;
      printf ("+");
    }else{
      Cnt--;
       printf ("-");
    }
  }
 // printf ("%ld\n",Cnt);
}

int main (void)
{
  wiringPiSetup () ;
  wiringPiISR (A_pin, INT_EDGE_BOTH, &AInterrupt) ;
  wiringPiISR (B_pin, INT_EDGE_BOTH, &BInterrupt) ;
	for(;;){
	}
  return 0 ;
}
