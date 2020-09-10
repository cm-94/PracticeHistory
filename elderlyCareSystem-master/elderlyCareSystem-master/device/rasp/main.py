#-*- encoding:utf-8 -*-
import time, random, sys, os, urllib2
import subprocess

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4 import *
from PyQt4 import uic
from PyQt4 import QtCore, QtGui
from weather import *
import paho.mqtt.client as mqtt
import pyautogui 
import click

##-- 온습도 센서 모듈 
import Adafruit_DHT
sensor = Adafruit_DHT.DHT11
pin = 4  # 온습도 센서 핀 

##---- MQTT
broker_address="localhost" 
broker_port=1883
client = mqtt.Client() #create new instance
client.connect(host=broker_address, port=broker_port)



class mainGUI(QDialog):
    

    def __init__(self, parent=None):
        QDialog.__init__(self, parent)
        # self.setWindowFlags(Qt.WindowStaysOnTopHint | Qt.FramelessWindowHint)
        
        ##---- PyQt관련 
        self.ui = uic.loadUi("main.ui")  # ui불러오기 

        self.ui.setWindowFlags(QtCore.Qt.FramelessWindowHint)   # 프레임 없애기 
        self.ui.setWindowFlags(QtCore.Qt.WindowStaysOnTopHint)   # 맨 앞으로 보내기 

        self.ui.show()
        # self.ui.showFullScreen()   # 전체화면 
        
        self.setGeometry(0, 0, 800, 460)  # 화면 사이즈 


        ##--- 쓰레드 선언 
        self.th = Thread_dht()  # dht
        self.th2 = Thread_weather()  # weather
        self.th3 = Thread_timer() # timer
        self.th4 = Thread_stream() # camera stream
        self.th5 = Thread_alone() # 독거노인
        self.th6 = Thread_dementia() # 치매환자

        ##--- 현재 날짜 세팅 , 함수 실행 , 쓰레드 실행 
        self.dateTimeVar = QDateTime.currentDateTime()  # 값 받아오기 
        self.timer_() # 타이머 실행
        self.displayDateTime() # 날짜 함수 실행 
        self.image()  # 이미지 함수 실행 
        self.dht() # 온도 센서 
        self.displayweather()  # 날씨 
        self.th4.start() # 카메라 스트리밍 실행 
        self.th5.start() # 독거노인 실행 
        self.th6.start() # 치매환자 실행 
       
        
        self.ui.pushButton.clicked.connect(self.google_talk)  # 버튼 눌렀을 때 구글 어시스턴트 실행 

   
    def google_talk(self):
        self.proc1 = subprocess.Popen('~/googletalk.sh', shell=True)
        # 누르면 스크립트 실행. 응답 시간후에는 자동 종료 


    def timer_(self):

        self.th3.start()  # 쓰레드 시작 

        #---- 시계
        # timer 초기화
        self.timer = QtCore.QTimer()
        
        # 업데이트 및 시작
        self.timer.timeout.connect(self.displayDateTime)
        self.timer.start(1000) # 1초다마 실행 

        #---- 날씨 
        self.Weathertimer = QtCore.QTimer()
        self.Weathertimer.setInterval(1000*60*60*3)      # 1000*60*60*3  세 시간 마다 실행 
        self.Weathertimer.timeout.connect(self.displayweather)
        self.Weathertimer.start()


    def displayDateTime(self) :
        
        # 현재 날짜 세팅 
        self.dateTimeVar = QDateTime.currentDateTime()  # 값 받아오기
        currenttime = self.dateTimeVar.toString("yyyy-MM-dd      hh:mm:ss")
        # 현재 날짜 넘기기 
        self.ui.label_time.setText(currenttime)

    def image(self):
        self.clockIcon = QPixmap()
        self.temIcon = QPixmap()
        self.humIcon = QPixmap()

        self.clockIcon.load("clock.png")  # 이미지 불러오기 
        self.temIcon.load("tem.png")
        self.humIcon.load("hum.png")


        self.clockIcon = self.clockIcon.scaledToWidth(80)  # 크기 조정
        self.temIcon = self.temIcon.scaledToWidth(80)
        self.humIcon = self.humIcon.scaledToWidth(80)

        self.ui.clockIcon.setPixmap(self.clockIcon)   # ui안에 넣기 
        self.ui.temIcon.setPixmap(self.temIcon)
        self.ui.humIcon.setPixmap(self.humIcon)

    def dht(self):  # 온습도 센서 

        self.th.start()  # 쓰레드 시작 

        self.th.change_value1.connect(self.ui.progressBar.setValue)
        self.th.change_value2.connect(self.ui.progressBar_2.setValue)

        #---- 35도 이상일경우 열사병 알림 
        # if()

    def displayweather(self):  
        self.th2.start()  # 쓰레드 시작 
 
        self.ui.label_weather_6.setText('현재온도 : '+NowTemp+'\n')
        self.ui.label_weather_5.setText(WeatherCast+'\n')
        if (WeatherCast.startswith('비')==True):
            self.ui.label_weather_4.setText(rain+'\n')
        else:
            self.ui.label_weather_4.setText('자외선 : '+TodayUV+'\n')
        # self.ui.label_weather_4.setText('자외선 : '+TodayUV+'\n')
        
        self.ui.label_weather_3.setText('미세먼지 : '+FineDust+'\n')
        self.ui.label_weather_2.setText('초미세먼지 : '+UltraFineDust+'\n')
        self.ui.label_weather.setText('오존 지수 : '+Ozon+'\n')



class Thread_dht(QThread):
    # 값이 변경되면 그 값을 change_value 시그널에 값을 emit 한다.
    change_value1 = pyqtSignal(int)
    change_value2 = pyqtSignal(int)

    def __init__(self):
        QThread.__init__(self)

    def run(self):

        while True:
            
            h, t = Adafruit_DHT.read_retry(sensor, pin)  
            html = urllib2.urlopen("https://api.thingspeak.com/update?api_key=KCKL36Q7DWG3F2OR&field1="+str(t)+"&field2="+str(h))       
            # thingspeak 시각자료를 위한 센서값 보내기 : API사용


            ht = str("{0}/{1}".format(h,t))
            client.publish("home/1/ht", ht)
            print('...')

            self.change_value1.emit(t)
            self.change_value2.emit(h)
            time.sleep(60*2)

            # client.publish("home/11/temp", str(t))  # home 의 온도 토픽
            # print("Temperature = {0:0.1f}*C".format(t))
            # time.sleep(15) # 30초 마다 전송 
            
            # client.publish("home/11/humid", str(h)) # home 의 습도 토픽 
            # print("Humidity = {}%".format(h))
            # time.sleep(15) # 30초 마다 전송 
            
            # self.msleep(100*30)


class Thread_weather(QThread):   ## weather.py  reload

    def __init__(self):
        QThread.__init__(self)

    def run(self):
            
        from weather import *
        import weather  
        reload(weather)   # 날씨를 3 시간 마다 다시 읽기 위해서 


class Thread_timer(QThread):   ## 타이머 

    def __init__(self):
        QThread.__init__(self)

    def run(self):
            
        mainGUI.dateTimeVar = QDateTime.currentDateTime()  # 값 받아오기



class Thread_stream(QThread):   ## 카메라 스트리밍 실행 

    def __init__(self):
        QThread.__init__(self)
        # self.Daemon = True

    def run(self):
        ##------------- 카메라 스트리밍 실행 
        # stream.sh 파일 없으면 만든다.  mjpg_streamer -i "input_raspicam.so " -o "output_http.so -p 8090 -w /usr/local/share/mjpg-streamer/www/"
        subprocess.call(['lxterminal -e sh stream.sh'], cwd='/home/pi/', shell=True)

class Thread_alone(QThread):   ## 독거노인 

    def __init__(self):
        QThread.__init__(self)

    def run(self):
            
     ##-------- 독거노인: 움직임 감지 실행  
        subprocess.Popen(['lxterminal -e python live_alone.py'], cwd='/home/pi/_GUI/', shell=True)


class Thread_dementia(QThread):   ##  치매환자

    def __init__(self):
        QThread.__init__(self)

    def run(self):
        
         ##-------- 치매환자: 야간 이상행동 감지 실행 
        subprocess.Popen(['lxterminal -e python dementia.py'], cwd='/home/pi/_GUI/', shell=True)


# class Thread_talk(QThread):   ## 구글 어시스턴트 실행 

#     def __init__(self):
#         QThread.__init__(self)
        
#     def run(self):
#         ##-------------
#         proc1 = subprocess.Popen('~/googletalk.sh', shell=True,  stdin=subprocess.PIPE, stdout=subprocess.PIPE)
        

if __name__ == '__main__':
    
    app = QApplication(sys.argv)    
    w = mainGUI()
    sys.exit(app.exec_())
    
