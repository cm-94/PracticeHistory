
# -*- coding: utf-8 -*- 
import datetime as dt
import sys, time, datetime
import os
import glob
import base64
import subprocess
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import Adafruit_DHT
import urllib2
sensor = Adafruit_DHT.DHT11
pin = 4
#---- 보내기

# broker_address="192.168.1.19" 
broker_address="localhost" 

broker_port=1883
client = mqtt.Client() #create new instance
print("connecting to broker....")
client.connect(host=broker_address, port=broker_port)

#---- 동영상 인코딩해서 전송한다.
subprocess.call("find /home/pi/_GUI/Move -type f -size -10k -delete", shell=True)  #아무것도 찍히지 않은 잉여 파일들 자동 삭제 

newest = max(glob.iglob('/home/pi/_GUI/Move/*.avi'), key=os.path.getctime)  # 제일 최신 파일 찾기 
todayfile = datetime.datetime.now().strftime("%y%m%d")  # 오늘 날짜

print(os.path.basename(newest))  # 최신 파일 이름 
mtime = os.path.getmtime(newest)  # 파일 생성일 
newest_make = datetime.datetime.fromtimestamp(mtime).strftime("%y%m%d")  # 만든날 
print(newest_make)
print (todayfile)

f  = open(newest)
encoded = base64.b64encode(f.read())


if(newest_make == todayfile):  # 최신 파일이 오늘 만든 파일이면 보낸다.
       client.publish("home/1/vid", encoded)
       print(".....Today video....")
              
else:       
       print('This is not today file..')
       pass

f.close()


# #---- 반복적으로 받아올 센서를 전송한다. 
# while True:
       # t, h=36, 50
       # th = str("{0}/{1}".format(t,h))
       # client.publish("home/1/temp",th)
       # print('...')
       # time.sleep(10) 
       # h,t = Adafruit_DHT.read_retry(sensor, pin)
       # client.publish("home/1/temp", str(t))  # home 의 온도 토픽
       # print("Temperature = {0:0.1f}*C".format(t))
       # # time.sleep(60*5) # 30초 마다 전송 
       
#        client.publish("home/1/humid", str(h)) # home 의 습도 토픽 
#        print("Humidity = {}%".format(h))
#        time.sleep(10) # 30초 마다 전송 

# print("Temperature = {0:0.1f}*C Humidity = {1:0.1f}%".format(t, h))