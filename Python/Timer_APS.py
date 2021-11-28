import time
import datetime
from apscheduler.schedulers.blocking import BlockingScheduler
pre_time=0;
def func():
    global pre_time
    now = time.time()
    cha=now-pre_time
    pre_time=now
    print(now," ",cha) 

scheduler = BlockingScheduler()
scheduler.add_job(func, 'interval', seconds=0.001, id='main')
scheduler.start()