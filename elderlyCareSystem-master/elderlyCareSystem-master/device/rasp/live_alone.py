# -*- coding: utf-8 -*- 
import cv2
import numpy as np
import sys, time, datetime
from datetime import timedelta
import paho.mqtt.client as mqtt

cap = cv2.VideoCapture('http://192.168.1.19:8090/?action=stream')   # wifi_스트리밍 영상 가져오기 
# cap = cv2.VideoCapture('http://192.168.1.35:8090/?action=stream')   # len_스트리밍 영상 가져오기 
# cap = cv2.VideoCapture('http://121.138.83.121:8090/?action=stream')   # wifi_외부_스트리밍 영상 가져오기 

# cap.set(cv2.CAP_PROP_BUFFERSIZE, 3)
# cap.set(cv2.CAP_PROP_FRAME_WIDTH, 480)
# cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 640)

##---- MQTT
broker_address="localhost" 
broker_port=1883
client = mqtt.Client() #create new instance
client.connect(host=broker_address, port=broker_port)

# Background Subtraction 의 알고리즘 중 BackgroundSubtractorMOG2 적용해 본다.
# BackgroundSubtractorMOG2
fgbg = cv2.createBackgroundSubtractorMOG2()
kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(4,4))  # 커널을 생성
# 얼굴 검출 
faceCascade = cv2.CascadeClassifier('/usr/local/share/opencv4/haarcascades/haarcascade_frontalface_default.xml')


# 파일 저장하기 
# frame_width, frame_height, frame_rate = int(cap.get(3)), int(cap.get(4)), 10
# fourcc = cv2.VideoWriter_fourcc(*'DIVX') # 디지털 포맷 코드 - 인코딩 방식 설정 
# out = cv2.VideoWriter('move2.avi', fourcc, frame_rate, (frame_width, frame_height), 0) # 캡쳐 동영상 저장
cnt = 0
contour_box = None 

StartDay = datetime.datetime.today() # 시작 시간 
# DDay = StartDay + timedelta(days=3)  # 이틀 뒤 날짜
DDay = StartDay + timedelta(seconds=10)  # 10초 뒤 날짜
global Hflag 
Hflag = 0                            # 사람이 인식 되었는지 0=안됨  1=됨

while(cap.isOpened()):
    NowDay = datetime.datetime.now()  # 현재 시간
    # print(StartDay)
    # print(NowDay)
    # print(DDay)

    ret, frame = cap.read()      # 배경 제거 프레임
    ret1, sub_frame = cap.read()  # 물체 검출할 서브 프레임 
        
    if (ret):
        
        fgmask = fgbg.apply(frame)
        
        fgmask = cv2.morphologyEx(fgmask, cv2.MORPH_CLOSE, kernel)  # 형태 변환 중 Closing 기법을 적용
        # fgmask = cv2.morphologyEx(fgmask, cv2.MORPH_OPEN, kernel)    # 형태 변환 중 Opening 기법을 적용

        # 물체 검출 / 임계처리 (src, 임계값, 임계값 넘었을 때 , 처리 타입 )
        ret1, thr = cv2.threshold(fgmask, 70, 255, cv2.THRESH_BINARY)
        
        # 흰색 테두리를 찾으며, 최소 point만 저장하여 메모리 절약 
        countors, hierarchy = cv2.findContours(thr, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        # cv2.drawContours(sub_frame,countors, -1, (0, 255, 0), 1)

        try:
            cnt = countors[-1]
            hull=cv2.convexHull(cnt)  # 대략적인 다각형 그리기 
            cv2.drawContours(sub_frame,[hull], -1, (0, 255, 0), 1)
            area=int(cv2.contourArea(cnt))
            # print(area)

        except:
            pass


        faces = faceCascade.detectMultiScale(
            sub_frame,
            scaleFactor=1.1,
            minNeighbors=5,
            minSize=(30,30),
            flags = cv2.CASCADE_SCALE_IMAGE 
        )

        if( area > 3000):  # 컨투어의 면적이 크면 글자표시 
            cv2.putText(sub_frame,'Detected!', (10, 30),
            cv2.FONT_HERSHEY_SIMPLEX, 1, (0,0,255), 2)

            
        for(x, y, w, h) in faces:
            cv2.rectangle(sub_frame, (x, y), (x+w, y+h), (0, 0, 255), 2)

            if (x+w > 0) and ( area > 3000):  # 사람이 인식 될 때 
                cv2.putText(sub_frame,'HUMAN Detected!', (10, 90),
                cv2.FONT_HERSHEY_SIMPLEX, 1, (0,0,255), 2)
                Hflag = 1

#-------------

        # if (Hflag == 1): # 사람이 인식 되었으면 시작 날짜와 이틀 후 날짜를 다시 세팅,  flag =0 
        #     StartDay = datetime.datetime.now() # 시작 시간 
        #     DDay = StartDay + timedelta(days=3)  # 이틀 뒤 날짜
        #     Hflag = 0                            # 사람이 인식 되었는지 0=안됨  1=됨

        # if(NowDay.days == DDay.days):  # 이틀동안 움직이지 않았다.
        #     print("119~~~~~~~~~~~~~")
        #     cv2.putText(sub_frame,'119~~!!', (30, 120),
        #     cv2.FONT_HERSHEY_SIMPLEX, 1, (0,255,255), 2)

        #     client.publish("home/11/alone", "1")    ##---- 녹화 끝나면 MQTT메시지 보내기 
        #     print("home/11/alone: ", "1") 
        #     Hflag = 1  # 알리고 다시 세팅 
            # import live_alone  # 버퍼링 

#-------------10초로 테스트 해보기 

        if (Hflag == 1): 
            StartDay = datetime.datetime.now() 
            DDay = StartDay + timedelta(seconds=10)  
            Hflag = 0                      
            import live_alone  # 버퍼링 제거     

        if(NowDay.second == DDay.second):  # 10초 동안 움직이지 않았다.
            print("119~~~~~~~~~~~~~\n")
            cv2.putText(sub_frame,'119~~!!', (30, 120),
            cv2.FONT_HERSHEY_SIMPLEX, 1, (0,255,255), 2)

            client.publish("home/1/alone", "1")    ##---- 녹화 끝나면 MQTT메시지 보내기 
            print("home/1/alone: ", "1")
            Hflag = 1  # 알리고 다시 세팅 
        #    import live_alone  # 버퍼링 

        # cv2.imshow('frame', fgmask)
        cv2.imshow("alone", sub_frame) 

        # out.write(fgmask)
   
    if cv2.waitKey(1) & 0xFF == ord('q'):  # q로 종료 
        break

cap.release()
# out.release()
cv2.destroyAllWindows()