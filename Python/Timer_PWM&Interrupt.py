import RPi.GPIO as GPIO
import time
import datetime

def my_callback1(channel):
    global pre_time
    global spin_count
    now = time.time()
    cha=now-pre_time
    pre_time=now
    spin_count += 1
    print(spin_count,cha)
    
pre_time=0;
spin_count = 0
In = 17
Out= 18
GPIO.setmode(GPIO.BCM)
GPIO.setup(In, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(Out, GPIO.OUT)
p = GPIO.PWM(Out, 100)
p.start(1) 
GPIO.add_event_detect(In, GPIO.RISING, callback=my_callback1)
while True:
    time.sleep(1)
