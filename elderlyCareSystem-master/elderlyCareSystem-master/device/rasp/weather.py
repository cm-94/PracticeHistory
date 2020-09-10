# -*- coding: utf-8 -*-
# -*- coding: euc-kr -*-
import requests 
from bs4 import BeautifulSoup

import sys # 한글 출력 안되서 
reload(sys)
sys.setdefaultencoding("utf-8")


Finallocation = '날씨'
LocationInfo = "" 
NowTemp = "" 
CheckDust = []

url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + Finallocation 
hdr = {'User-Agent': ('mozilla/5.0 (windows nt 10.0; win64; x64) applewebkit/537.36 (khtml, like gecko) chrome/78.0.3904.70 safari/537.36')} 
req = requests.get(url, headers=hdr) 
html = req.text 
soup = BeautifulSoup(html, 'html.parser')

# 현재
NowTemp = soup.find('span', {'class': 'todaytemp'}).text + soup.find('span', {'class' : 'tempmark'}).text[2:]

# 날씨 캐스트
WeatherCast = soup.find('p', {'class' : 'cast_txt'}).text

#자외선
TodayUV = soup.find('span', {'class' : 'indicator'}).text[4:-2] + " " + soup.find('span', {'class' : 'indicator'}).text[-2:]
# TodayUV = soup.find('span', {'class' : 'lv4'}).text

# 비
rain = soup.find('span', {'class' : 'rainfall'}).text

# 현재 사는 위치  
nowlocal = soup.find('span', {'class' : 'btn_select'}).text

# 미세먼지, 초미세먼지, 오존 지수 
CheckDust1 = soup.find('div', {'class': 'sub_info'}) 
CheckDust2 = CheckDust1.find('div', {'class': 'detail_box'}) 
for i in CheckDust2.select('dd'): 
    CheckDust.append(i.text) 

FineDust = CheckDust[0][:-2] + " " + CheckDust[0][-2:] 
UltraFineDust = CheckDust[1][:-2] + " " + CheckDust[1][-2:] 
Ozon = CheckDust[2][:-2] + " " + CheckDust[2][-2:]

print('현재 위치: '+ nowlocal)
print('현재온도 :'+NowTemp)
print(WeatherCast)

if (WeatherCast.startswith('비')==True):
    print(rain)  # 비 
else:
    print('자외선: '+TodayUV)

# print('자외선: '+TodayUV)
# print('자외선: '+rain)  # 비 


print('미세먼지 :'+FineDust)
print('초미세먼지 :'+UltraFineDust)
print('오존 지수 :'+Ozon)

# print('reload')

