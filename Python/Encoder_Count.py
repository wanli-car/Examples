import RPi.GPIO as GPIO
import time

spin_count = 0
A = 5
B = 6
GPIO.setmode(GPIO.BCM)
GPIO.setup(B, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(A, GPIO.IN, pull_up_down=GPIO.PUD_UP)

def my_callback1(channel):
    global spin_count
    if GPIO.input(A):
        if GPIO.input(B):
            spin_count += 1
        else:
            spin_count -= 1
    else:
        if GPIO.input(B):
            spin_count -= 1
        else:
            spin_count += 1
    print("A",spin_count)
            
def my_callback2(channel):
    global spin_count
    if GPIO.input(B):
        if GPIO.input(A):
            spin_count -= 1
        else: 
            spin_count += 1    
    else:
        if GPIO.input(A):
            spin_count += 1
        else:
            spin_count -= 1
    print("B",spin_count)
GPIO.add_event_detect(A, GPIO.BOTH, callback=my_callback1)
GPIO.add_event_detect(B, GPIO.BOTH, callback=my_callback2)
while True:
    time.sleep(1)
