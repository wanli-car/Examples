#include "Control.h"

struct Motor M1={
   .Pin_A=21,
   .Pin_B=22,
};
struct Motor M2={
   .Pin_A=23,
   .Pin_B=24,
};
struct Motor M3={
   .Pin_A=28,
   .Pin_B=29,
};
struct Motor M4={
   .Pin_A=25,
   .Pin_B=27,
};

float Speed_Kp=255,Speed_Ki=100;
float Speed=0;
float Set_PWM_float=0;
uint8_t Set_PWM=0; 
Emakefun_MotorShield Pwm ;
//外部中断回调函数，用来计数电机速度
void Interrupt_M1 (void) {
   M1.Encoder_Cnt++;
}
void Interrupt_M2 (void) {
   M2.Encoder_Cnt++;
}
void Interrupt_M3 (void) {
   M3.Encoder_Cnt++;
}
void Interrupt_M4 (void) {
   M4.Encoder_Cnt++;
}
//电机驱动初始化
void Motor_Init(){
     Pwm = Emakefun_MotorShield();
	Pwm.begin(50);
	 M1.Handle = Pwm.getMotor(1);
	 M2.Handle = Pwm.getMotor(2);
	 M3.Handle = Pwm.getMotor(3);
	 M4.Handle = Pwm.getMotor(4);
	M1.Speed_Set=0.5;
	M2.Speed_Set=0.5;
	M3.Speed_Set=0.5;
	M4.Speed_Set=0.5;
	M1.Direction=FORWARD;
	M2.Direction=FORWARD;
	M3.Direction=FORWARD;
	M4.Direction=FORWARD;
}
//编码器驱动初始化
void Encoder_Init(){
    wiringPiSetup () ;
  	wiringPiISR (M1.Pin_A, INT_EDGE_BOTH, &Interrupt_M1) ;
  	wiringPiISR (M1.Pin_B, INT_EDGE_BOTH, &Interrupt_M1) ;
	wiringPiISR (M2.Pin_A, INT_EDGE_BOTH, &Interrupt_M2) ;
  	wiringPiISR (M2.Pin_B, INT_EDGE_BOTH, &Interrupt_M2) ;
	wiringPiISR (M3.Pin_A, INT_EDGE_BOTH, &Interrupt_M3) ;
  	wiringPiISR (M3.Pin_B, INT_EDGE_BOTH, &Interrupt_M3) ;
	wiringPiISR (M4.Pin_A, INT_EDGE_BOTH, &Interrupt_M4) ;
  	wiringPiISR (M4.Pin_B, INT_EDGE_BOTH, &Interrupt_M4) ;
}

void Get_Speed(long Time_Period)
{
	M1.Speed_Now=(float)M1.Encoder_Cnt/Time_Period*50;
	M1.Encoder_Cnt=0;
	M2.Speed_Now=(float)M2.Encoder_Cnt/Time_Period*50;
	M2.Encoder_Cnt=0;
	M3.Speed_Now=(float)M3.Encoder_Cnt/Time_Period*50;
	M3.Encoder_Cnt=0;
	M4.Speed_Now=(float)M4.Encoder_Cnt/Time_Period*50;
	M4.Encoder_Cnt=0;
}


//电机速度PI控制函数
float Speed_PI(float * Encoder_Integral,float Now_Speed,float Set_Speet)
{
	float fP;
	fP=Set_Speet-Now_Speed;
	*Encoder_Integral+=fP;
	if(*Encoder_Integral>SPEED_INTEGRAL_MAX)  	*Encoder_Integral=SPEED_INTEGRAL_MAX;           
	return fP*Speed_Kp+(*Encoder_Integral)*Speed_Ki;  
}


void Data_Process(char * Data){
	float gen=2.55;
	if(Data[0]>128){
		M1.Motor_out=(256-Data[0])*gen;
		M1.Direction=2;
	}else if(Data[0]==0){
		M1.Motor_out=0;
		M1.Direction=3;		
	}
	else{
		M1.Motor_out=Data[0]*gen;
		M1.Direction=1;
	}
	
	if(Data[1]>128){
		M2.Motor_out=(256-Data[1])*gen;
		M2.Direction=2;
	}else if(Data[1]==0){
		M2.Motor_out=0;
		M2.Direction=3;		
	}else{
		M2.Motor_out=Data[1]*gen;
		M2.Direction=1;
	}
	
	if(Data[2]>128){
		M3.Motor_out=(256-Data[2])*gen;
		M3.Direction=2;
	}else if(Data[2]==0){
		M3.Motor_out=0;
		M3.Direction=3;		
	}else{
		M3.Motor_out=Data[2]*gen;
		M3.Direction=1;
	}

	if(Data[3]>128){
		M4.Motor_out=(256-Data[3])*gen;
		M4.Direction=2;
	}else if(Data[3]==0){
		M4.Motor_out=0;
		M4.Direction=3;		
	}else{
		M4.Motor_out=Data[3]*gen;
		M4.Direction=1;
	}
}

//执行电机控制输出
void Out_Motro(){

	M1.Handle->my_run(M1.Motor_out,M1.Direction);
	M2.Handle->my_run(M2.Motor_out,M2.Direction);
	M3.Handle->my_run(M3.Motor_out,M3.Direction);
	M4.Handle->my_run(M4.Motor_out,M4.Direction);
}
//
void Speed_Control(){
	M1.Motor_out_float=Speed_PI(&M1.Encoder_Integral,M1.Speed_Now,M1.Speed_Set);
	M2.Motor_out_float=Speed_PI(&M2.Encoder_Integral,M2.Speed_Now,M2.Speed_Set);
	M3.Motor_out_float=Speed_PI(&M3.Encoder_Integral,M3.Speed_Now,M3.Speed_Set);
	M4.Motor_out_float=Speed_PI(&M4.Encoder_Integral,M4.Speed_Now,M4.Speed_Set);
	if (M1.Motor_out_float>255){M1.Motor_out=255;}
	else if(M1.Motor_out_float<0){M1.Motor_out=0;}
	else{M1.Motor_out=(uint8_t)M1.Motor_out_float;}

	if (M2.Motor_out_float>255){M2.Motor_out=255;}
	else if(M2.Motor_out_float<0){M2.Motor_out=0;}
	else{M2.Motor_out=(uint8_t)M2.Motor_out_float;}

	if (M3.Motor_out_float>255){M3.Motor_out=255;}
	else if(M3.Motor_out_float<0){M3.Motor_out=0;}
	else{M3.Motor_out=(uint8_t)M3.Motor_out_float;}

	if (M4.Motor_out_float>255){M4.Motor_out=255;}
	else if(M4.Motor_out_float<0){M4.Motor_out=0;}
	else{M4.Motor_out=(uint8_t)M4.Motor_out_float;}
}

void Do_Motor(long Time_Period){
	Get_Speed( Time_Period);
	Speed_Control();
	Out_Motro();
printf ("%ld,%f,%d,%f\n",Time_Period,M1.Speed_Now,M1.Motor_out,M1.Encoder_Integral);
printf ("%f,%f,%f,%f\n",M1.Speed_Now,M2.Speed_Now,M3.Speed_Now,M4.Speed_Now);
}