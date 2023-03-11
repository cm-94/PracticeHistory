# Practice History
 Android - Web Practice Repository


## Android Practice
 - Elderly Care System
 
 - Book Search Practice
 
 - BitCoin Api Practice
  https://github.com/cm-94/PracticeHistory/cm/Bithumb/
  
 - Candle Chart Draw Practice
  https://github.com/cm-94/PracticeHistory/cm/ChartPractice/
  
## Web
 - Practice of Supply Chain System
  https://github.com/cm-94/PracticeHistory/tree/master/cm/pscm


# Android
  
  
## 1. Elderly Care System
 첫번째, 그리고 아직 완성되지 않은 프로젝트.
 우린 젊을 떄는 가족과 친구들과 함께하지만, 언젠가 홀로 죽을 수 있습니다. 그래서 노인의 독거사를 예방하는 것은 반드시 고려해야 할 사회 안전망입니다.
 우리는 세 연결을 통해 구현하려 합니다.

 It's my first and still unfinished project.
 When we are young, We are with our families and friends but after then can die alone anytime. So preventing the elderly from dying alone must be considered a necessary social safety net. We try to make this three connection for it. 

### Path
- https://github.com/cm-94/PracticeHistory/tree/master/cm/ElderlyCareSystem
- https://github.com/cm-94/PracticeHistory/tree/master/cm/ElderlyVersion

### Tech
- Language : JAVA
- Tool : Android Stutio
- lib : OkHttp3, Retrofit2, GoogleMap. Push ( google - FCM )

### Main Connection
 [ Smart Watch ] - ( bluetooth ) - [ Android ] - ( HTTP / Rest Api ) - [ Server ]
 
 - 시계는 주인의 동작과 움직임, GPS, 심박수를 측정합니다. 그리고 매 분마다 노인의 휴대폰에 전송합니다. ( 데이터 타입 : String - JSON 포맷 )
 - 노인의 안드로이드 폰은 데이터를 받아 서버로 전송합니다 ( Rest Api )
 - 사회복지사는 담당 노인의 상태와 위치를 모바일로 확인 가능합니다. ( 구글 맵 )
 - 노인에게 문제 발생 (심박수 저하, 움직임 감지 불능 등) 시 사회복지사의 앱으로 Push가 전송됩니다.
 
 - The watch measures the owner's movements, gyroscope, gps and heart rate. It is sent to elderly's mobile phone every minute. ( Data type : String - JSON Format )
 - Elderly's Android check received data and send to server.
 - Social workers can check the elderly list and state of each one in charge on mobile. ( Google Map )
 - When a problem occurs in the elderly (low heart rate, inability to detect motion, etc.), a push is sent to the social worker's app.

### Comment..
 - 안드로이드 개별 학습을 하며 진행한 임베디드 시스템 최종 프로젝트 였으나 몇가지 오류가 발생하였다.
 - 1. 시계에서 보내는 String 데이터의 검증이 완전하지 않다. JSON 포맷으로 전송하기 때문에 "{" 와 "}" 를 기준으로 데이터 패킷을 나누었다.
 -  보완 대책으로 Byte크기별 패킷을 나누어 다시 통신 구조를 작성해 볼 수 있다고 판단된다.
 - 2. push가  전송되지 않는 케이스가 발생한다.
 -  앱을 오래 꺼두면 OS에서 프로세스를 제거하는 것으로 추측 되지만 현재 테스트 상태 구현을 위해 Spring 서버를 만들어야 한다..

## Book Search Practice
 ### 도서 검색 
 - 언어 : Kotlin
 - 도구 : 안드로이드 스튜디오
 - 기술 스택 : okhttp3, retrofit2
 - Api : 네이버 책 검색 api 사용 ( https://openapi.naver.com/v1/search/ )
  로그인 필요 x. 성인 검색어 판별. 오타 변환 기능이 한국어 검색에 유리하다고 판단.
 
 ### Main
 - 도서 검색 및 BookAdapter ( RecyclerView ) 를 통한 리스트 생성
 - 하단 도달 시 다음 데이터 조회. 데이터 클릭 시 상세화면 이동
 - 앱 강제 종료 & 버그 상황 시 앱 재실행. 이때 이전 검색어를 가져오기 위해 onSaveInstanceState override.
 
 ### Info
 - FragmentAdapter  - BookFragment 생성
 - 링크를 통해 WebChromeClient로 화면 생성.
 - 주소 복사 및 공유하기 기능.

 ### Path
 - https://github.com/cm-94/PracticeHistory/cm/Practice/BookSearch/
