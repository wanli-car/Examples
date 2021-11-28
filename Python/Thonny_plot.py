from time import sleep
from random import randint

p1 = 0
while True:
    p1 += randint(-1, 1)
    p2 = randint(-10, 10)
    print("Random walk:", p1, " just random:", p2)
    sleep(0.05)