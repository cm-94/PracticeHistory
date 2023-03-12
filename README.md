# Practice History
 Android - Web Practice Repository


## Android Practice
 - Elderly Care System
 
 - Book Search Practice
 
 - BitCoin Current Price
  
 - Candle Chart Draw Practice

## Web Practice
 - Panopticon Supply Chain Management


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
 - 2. push가  전송되지 않는 케이스가 발생한다.
 -  보완 및 대책
 - 1. Byte 크기별 패킷을 나누어 다시 통신 구조를 작성해 볼 수 있다고 판단된다.
 - 2. 앱을 오래 꺼두면 OS에서 프로세스를 제거하는 것으로 추측 된다. 현재 테스트를 위해 다시 Spring 서버를 구축해야 한다..
 


## 2. Book Search Practice
 ### 도서 검색 및 공유
 - 언어 : Kotlin
 - 도구 : 안드로이드 스튜디오
 - 기술 스택 : okhttp3, retrofit2
 - Api : 네이버 책 검색 api 사용 ( https://openapi.naver.com/v1/search/ ). 
 
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



## 3. BitCoin Api Practice
 ### 비트코인 현재가 조회 어플
 - 언어 : Kotlin
 - 도구 : 안드로이드 스튜디오
 - 기술 스택 : okhttp3, retrofit2
 - Api : Bithumb Api 사용 ( https://api.bithumb.com/public/ ). 
 
   단순하게 알고 있는 비트코인 거래소가 빗썸 뿐이라 사용하게 되었다. 검색해보니 바이낸스 등 다른 open Api들 마다 특징이 있는 것 같다.
 
 ### Main
 - ViewPager 사용 ( 3페이지. )
 - 비트코인 종목 조회 및 환율 별 표시
 - 기본 종목 : BTC 설정. ( 대표 코인 )
 - 당일 시가 ( 12시 종가 ) 기준 등락률 상승 => 빨간색 / 하락 => 파란색
 
 ### Comments..
 - 기존 연습 어플보다 나는 api를 작성하고 화면 구성을 하는 등 보안을 하면, 좀 더 괜찮게 보여줄 수 있을 것 같다.
 - 실시간처럼 보이도록 api를 초당 30회 이상 요청 할 경우 최대 요청 횟수를 초과한다. 그렇다면 어떻게 기준을 세워야 좋을지 생각해보자.
 - 전체 현재가를 가져온 다음 adapter를 초기화한다. 이렇게 등락률에 따른 색을 변경할 경우 자원 사용에 매우 비효율적이다. Adapter에 event를 추가하여 n번째 item만 수정되도록 해보자.
 ### Path
 - https://github.com/cm-94/PracticeHistory/cm/Bithumb/



## 4. Candle Chart ( Custom View ) Draw Practice
 ### Custom View 연습 ( 캔들차트 )
 - 언어 : Kotlin
 - 도구 : 안드로이드 스튜디오
 - 목적 1. paint & canvas를 사용해 주식 데이터 ( 시고저종 가 )를 화면에 그려본다.
 
 ### Main
 - 임의의 데이터에 대한 봉차트 그리기 연습
 - 해결과제 1. 등락에 따른 색 설정 필요.
 - 해결과제 2. 최저 ~ 최대 값으로 Layout 내 봉 & 바 높낮이 값 세팅 필요.
 
 ### Comments..
 - 1. 색 설정 : 종가 > 시가 ( 빨강 ) / 종가 < 시가 ( 파랑 ) / 종가 == 시가 ( 검정 ) 이므로 어렵지 않다.
 - 2. view는 width / height 뿐만 아니라 margin / padding 값을 가지고 있다. 이를 모두 반영하는 view를 만들려다 계산이 어긋나는 실수가 있었다.
 - 보완
 - 1. 실제 api를 통해 데이터 받아오기.
 - 2. 데이터 수에 따른 봉 사이 간격 조정 등 수정.
 - 3. 스크롤, 확대 축소 등 추가 기능에 대한 방법 생각해보기.
 
 ### Path
 - https://github.com/cm-94/PracticeHistory/cm/ChartPractice/


# Web

## Panopticon Supply Chain Management
 ### 판옵티콘샵 재고관리 시스템
 - 언어 : Html, Css, Js. 
 - 도구 : Visual Code
 - 기술 스택 : Node Js. AWS
 - 목적 : 취업 후 업무적으로 서버와 소통해야 되는 일이 종종 발생했다. 서비스가 확장되거나 변경될 때 서버에 해당 Api / Transaction ( Http / 소켓 통신 )에 대해 문의해야 했다. 이때 종종 소통에 어려움이나, 또 서로 이해하지 못하는 부분이 있다고 느껴졌다.
 
   이를테면 주문내역 조회의 경우, 국내 주식 서비스 중 해외주식 서비스가 추가될 경우 두 종목의 차이에 의한 데이터의 부재가 생기거나 ( 종목 코드가 다르거나, 환율정보가 필요하다는 등 )
   
   기존 api에 누락된 정보가 있다고 판단해 문의했으나 DB에선 다른 테이블에 정의되어 있다는 등..
   
   그래서 직접 DB ~ Client 까지 이어지는 사이드 프로젝트를 만들어 보기로 했다
 
 ### Main
 - 화면은 메인에 총 4개의 메인 탭으로 구성한다
 
 [상품 조회 및 등록] [ 재고 조회 및 출납장부 작성 ] - [ 거래처 조회 및 등록 ] - [ 미정 ]
 
 - DB 는 AWS를 사용했으나 PC에 직접 DB를 만들었다.
 - 외부 접속을 위해 가정 내 공유기를 통해 포트포워딩 하는 것으로 바꾸었다.
 - Node JS 교육받은 경헙이 있어 사용하였다.

 - 기능
     1. 상품
     - 상풍 리스트 조회.
     - 상품 추가 팝업 -> 제품사진, 제품명, 가격, 바코드, 특이사항 등 작성 가능.
     
     2. 재고
     - 재고 조회.
     - 제품별 입,출고 추가 등록. 화살표 클릭 시 해당 Row 하단에 
     
     1. 상품
     - 상풍 리스트 조회.
     - 상품 추가 팝업 -> 제품사진, 제품명, 가격, 바코드, 특이사항 등 작성 가능.
     
     4. 미정
     - 추후 삭제내역 조회 화면으로 만들 계획
     - 상품 및 거래처 삭제 시 해당 이력을 조회하도록 한다.
     
 
 ### Comments..
 - 집 와이파이가 자꾸 끊기는 바람에 외부 접속 가능여부가 무색해졌다.. 따라서 현재 외부에서 사용 불가능..
 - 비전공자로서 임베디드 교육 ~ 취업 후 모바일 업무만 하다보니 접할 수 없던 분야 ( DB 구성부터 SQL 등 )에 대한 학습의 시간이 의미있었다.
 - 초반에 구성한 상품 정보를 통해 DB 와 상품 화면을 만들고, 재고 화면을 만들려니 상품 테이블에 재고도 필요할 것 같고, 입출고 예정 수량도 필요해 보이고..
     중간에 수정하다 보니 상품 테이블을 조회하는 SQL 전부 수정되고..
 - DB 공부를 제대로 했다고 할 수는 없을 수 있으나, 적어도 업무나 프로젝트에 있어 그들과 조금은 더 공감하고 소통할 수 있을 것 같다. 어쩌면 내가 도움이 될 수도 있다고 생각하니 정말 좋은 경험이 아니었나 생각한다.
 
 ### Path
 - https://github.com/cm-94/PracticeHistory/tree/master/cm/pscm
