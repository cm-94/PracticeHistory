# spring

* 경량 컨테이너로서 자바 객체를 직접 관리
* 제어 역행 (IOC Inversion of Control)
* 의존성 주입(DI Dependency Injection)
* 관점지향 프로그래밍(AOP Aspect-Oriented Programming)
* MVC패턴
----->공통부분의 코딩 용이, 확장성 높음.


DI Dependency Injection
의존성 : unit test와 코드 수정이 어려워진다.
가령 class명을 변경하게 된다면 해당 클래스가 사용된 모든 클래스를 수정해야 한다.

이러한 이유로 스프링은 DI를 사용해 모듈간의 결합도를 낮춘다.
* 의존성을 대신 주입 할 bean class작성
* 주입을 위한 xml또는 @(어노테이션) 설정

IOC Container(Spring Container)
사용자가 작성한 메타데이터(xml또는 @(어노테이션))에 따라 bean 클래스를 생성 및 관리하는 spring의 컴포넌트를 말한다.

* IOC Container 설정
1. xml 파일 기술
	: code와 의존성을 주입하는 부분을 분리할 수 있다. 유지보수가 쉽다. 각 객체간의 의존관계를 한눈에 파악할 수 있다. 그러나 시스템이 거대해진다면 xml 파일이 많아져 오히려 유지보수가 어려울 수 있다.
2. @annotation 사용
	: 프로그램의 규모가 커지면서 xml에 기술할 내용이 많아짐에 따라 더 효율적인 annotation이 등장하게 된다. 직관적인 코드 작성이 가능하다.

@Configuration : 스프링 IOC container에게 해당 클래스가 bean 구성 클래스임을 알려줌
@Bean : 외부 라이브러리를 bean으로 만들고자 할 때 사용
@Component : 개발자가 직접 작성한 Class를 Bean으로 등록하기 위해 사용
@AutoWired :  Component 를 사용한 Bean의 의존성 주입은 AutoWired를 사용하여 의존성 자동 주입 가능

# MQTT
### Message Queuing Telemetry Transport

IoT기기, 모바일 기기에 최적화 된 가벼운 메세징 프로토콜

IoT 기기(publisher) --data--> MQTT브로커 --data--> IoT기기, 컴퓨터, 스마트폰(subscriber)

사물인터넷을 사용하기 위해 개발 된 TCP기반의 프로토콜로서 낮은 전력, 대역폭, 성능의 환경에서도 사용 가능하다.

publisher가 broker에게 데이터를 전송하면 subscriber에서 데이터를 받아온다.

어디로 데이터를 받을 지 정해주는 것이 Topic이다. publisher가 데이터에 대해 topic을 정하면 그 topic을 구독하는 subsciber는 해당 데이터를 받는다.



참고:<br>
https://www.joinc.co.kr/w/man/12/MQTT/Tutorial<br>
http://www.codejs.co.kr/mqtt-%ec%9d%b4%ed%95%b4%ed%95%98%ea%b8%b0/<br>
https://khj93.tistory.com/entry/MQTT-MQTT%EC%9D%98-%EA%B0%9C%EB%85%90


## mqtt client 구현하기 in java
내용 출처 : https://www.baeldung.com/java-mqtt-client

paho library 의존성 추가
```
<dependency>
  <groupId>org.eclipse.paho</groupId>
  <artifactId>org.eclipse.paho.client.mqttv3</artifactId>
  <version>1.2.0</version>
</dependency>
```

client에서 메세지를 주고 받기 위해서는 IMqttClient interface가 필요하다.
이 interface는 서버와 연결을 위한 메소드, 그리고 메세지를 주고 받을 수 있는 메소드를 가지고 있다.

1. MqttClient클래스의 인스턴스 생성

```java
String publisherId = UUID.randomUUID().toString();
IMqttClient publisher = new MqttClient("tcp://iot.eclipse.org:1883",publisherId);
```

2. 서버에 연결
`MqttConnectOptions` 클래스를 통해 인스턴스 선택적으로 전달.<br>
보안 자격 증명, 세션 복구 모드, 재 연결 모드 등과 같은 추가 정보 전달 가능.<br>
필요한 옵션만 설정하고 나머지는 기본값으로 가정한다.<br>
```java
MqttConnectOptions options = new MqttConnectOptions();
options.setAutomaticReconnect(true);
options.setCleanSession(true);
options.setConnectionTimeout(10);
publisher.connect(options);
```
위의 코드는 해당 내용을 나타낸다.<br>
* 네트워크 장애 시 라이브러리가 서버에 자동으로 다시 연결을 시도한다.
* 이전 실행에서 보내지 않은 메세지는 버린다.
* timeout은 10초

3. 메세지 보내기
mqtt는 3단계 QoS(Quality of Service)를 제공한다.<br>
0 - 메세지는 한번만 전달되며, 전달 여부는 확인하지 않는다. Fire and Forget.<br>
1 - 메세지는 반드시 적어도 한 번 전달된다. 값이 중복전달 될 수 있다. subscriber가 중복값을 처리할 수 있는 경우 이 옵션을 써도 된다.<br>
2 - 메세지는 정확히 한 번만 전달된다. 메세지 핸드셰이킹 과정을 추적하기 때문에 품질이 높지만 성능이 떨어질 수 있다. subscriber가 중복값을 처리하기 어려운 경우 사용하면 좋다.<br>

```java
public class EngineTemperatureSensor implements Callable<Void> {
 
    // ... private members omitted
     
    public EngineTemperatureSensor(IMqttClient client) {
        this.client = client;
    }
 
    @Override
    public Void call() throws Exception {        
        if ( !client.isConnected()) {
            return null;
        }           
        MqttMessage msg = readEngineTemp();
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(TOPIC,msg);        
        return null;        
    }
 
    private MqttMessage readEngineTemp() {             
        double temp =  80 + rnd.nextDouble() * 20.0;        
        byte[] payload = String.format("T:%04.2f",temp)
          .getBytes();        
        return new MqttMessage(payload);           
    }
}
```

4. 메세지 받기
```java
CountDownLatch receivedSignal = new CountDownLatch(10);
subscriber.subscribe(EngineTemperatureSensor.TOPIC, (topic, msg) -> {
    byte[] payload = msg.getPayload();
    // ... payload handling omitted
    receivedSignal.countDown();
});    
receivedSignal.await(1, TimeUnit.MINUTES);
```


# Spring project 

## mvc project directory 구조

src/main/java : 자바 소스파일 디렉토리<br>
src/main/resources : 리소스파일(설정파일) 디렉토리<br>
	src/main/resources/log4j.xml : 로그파일<br>
<br>
src/test/java : 테스트 파일 디렉토리<br>
src/test/resources : 테스트 리소스 파일 디렉토리<br>
<br>
Maven Dependencies : maven을 통해 다운받은 라이브러리 파일<br>
<br>
src/main/webapp/resources : 리소스파일 디렉토리(js, css, image등)<br>
<br>
WEB-INF 외부 직접 접근 차단. 컨드롤러를 통하여 접근 가능.<br>
	src/main/webapp/WEB-INF/classes : 클래스 파일 디렉토리<br>
	src/main/webapp/WEB-INF/spring : 스프링 환경 설정 파일 디렉토리 (root-context.xml, servlet-context.xml) (서블릿 파일)<br>
		src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml : dispatcher 서블릿과 관련된 view 지원 및 bean을 설정하는 파일(controller등)<br>
		src/main/webapp/WEB-INF/spring/root-context.xml : 공통 bean을 설넝하는 파일(service, repository, db, log등)<br>
	src/main/webapp/WEB-INF/views : view파일 디렉토리(html, jsp) (jsp파일 폴더)<br>
	src/main/webapp/WEB-INF/web.xml : 설정을 위한 배포서술자 파일. WAS가 최초 구현될 때 web.xml을 읽고 해당 설정 구현. 설정파일.<br>
<br>
target : 빌드 결과물<br>
<br>
pom.xml : maven에서 참조하는 설정파일<br>


### mybatis
개발자가 지정한 SQL, 저장 프로시저 그리고 몇가지 고급 매핑을 지원하는 퍼시스턴스 프레임워크 
https://mybatis.org/mybatis-3/ko/index.html

### interceptor
컨트롤러에서 들어오는 HttpRequest와 컨트롤러가 응답하는 HttpResponse를 가로채는 역할을 한다.


# RESTful

### open api 개방형 api
프로그래밍에서 사용할 수 있는 개방되어 있는 상태의 인터페이스

### rest representational safe tranfer

https://www.youtube.com/watch?v=pKT4OTFjFcA&list=PL9mhQYIlKEhfYqQ-UkO2pe2suSx9IoFT2&index=24

http uri + http method
http uri를 통해 제어할 자원 resource를 명시,
http method (get, post, put, delete)를
통해 해당 자원을 제어하는 명령을 내리는 방식의 ^아키텍쳐^

| http method | CRUD |
|:--------|:--------|
| POST | create(insert) |
| GET | read(select) |
| PUT | update |
| DELETE | delete |

rest의 원리를 따르는 시스템을 restful 용어 사용
ex
GET /list.do? no=510&name=java (query string)
----> GET /bbs/java/510
GET /delete.do?no=510&name=java
----> DELETE /bbs/java/510
GET과 POST만으로 자원에 대한 CRUD를 처리, uri는 액션을 나타내는 기존의 상태에서
4가지 메서드를 사용하여 CRUD처리, uri는 제어하려는 자원 나타내도록

RESTful과 JSON/xml

json은 경량(lightweight)의 data교환 형식
javascript에서 객체를 만들때 사용하는 표현식
특정 언어에 종속되지 않는다.
```json
{
	"string1": "value1",
	"string2": "value2",
	"string3": ["value3", "value4"]
}
```

json 라이브러리 jackson
json을 java객체로 java를 json형태로 변환해주는 json라이브러리

xml extensible markup language
데이터 저장 전달 위한 언어

xml과 html
data전달하는 것에 포커스 - data를 표현하는 것에 포커스
사용자가 마음대로 tag정의 가능 - 미리 정의된 tag만 사용 가능
```xml
<?xml version = "1.0" encoding = "UTF-8"?>
<customer>
	<name>김형희</name>
	<addr>진안</addr>
	<phone>01000000000</phone>
</customer>
```


## spring mvc 기반 restful 웹서비스 환경 설정, 구현
pom.xml
jackson mapper
```xml
<!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl -->
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>1.9.13</version>
</dependency>
```

root-context.xml
`<mvc:annotation-driven />`
`<mvc:default-servlet-handler/>`서버에 내부적으로 정의된 /무시

구현
1. RESTful 웹 서비스 처리할 RESTfulController클래서 작성, Spring Bean등록
2. 요청 처리할 메서드에 @RequestMapping, @ReqeustBody(json ->java)와 @ResponseBody(java->json)어노테이션 선언
3. REST Client Tool(Postman)을 사용하여 각각의 메서드 테스트
4. Ajax통신을 하여 RESTful 웹서비스 호출하는 HTML페이지 작성

Postman 설치 (RESTAPI 테스트 하는 Chrome 확장 프로그램)

* 사용자관리

| Action | Resource URI | HTTP Method |
|:--------|:--------|:--------|
| 사용자 목록 | /users | GET |
| 사용자 보기 | /users/{id} | GET |
| 사용자 등록 | /users | POST |
| 사용자 수정 | /users | PUT |
| 사용자 삭제 | /users/{id} | DELETE |

@RequestBody : HTTP Request Body를 java 객체로 전달받을 수 있다.
@ResponseBody : Java객체를 HTTP Response Body로 전송할 수 있다.


오류

`org.springframework.http.converter.HttpMessageNotWritableException: No converter found for return value of type: class com.spring.elderlycare.dto.MemberDTO`
controller에서 객체 반환시 json으로 변환되지 않음.
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.10.0</version>
</dependency>
		    <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.10.0</version>
</dependency>
```

```xml
<mvc:annotation-driven>
		<mvc:message-converters>
		<bean class = "org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/>
		</mvc:message-converters>
	</mvc:annotation-driven>
```

### session
클라이언트가 서버 접속하는 순간 생성
default 유지시간 30분(서버에 접속 후 요청 하지 않는 최대 시간)

web.xml파일에서 직접 설정 가능

```xml
<session-config>
	<session-timeout>30</session-timeout>
</session-config>
```

@SessionAttributes 파라미터 지정된 이름이 model에 저장 되면 session에도 저장됨.





### Spring AOP
Aspect Oriented Programming 관점 지향 프로그래밍
* Aspect: 흩어진 관심사들을 모듈화 한 것. 주로 부가기능을 모듈화 한 것.
* Target: Aspect를 적용하는 곳(클래스, 메서드 등)
* Advice: 실질적으로 어떤 일을 해야 할 지에 대한 것, 실질적 부가기능을 담은 구현체
* JointPoint: Advice가 적용될 위치, 끼어들 수 있는 지점. 메서드 진입 지점, 생성자 호출 시점, 필드에서 값을 꺼내올 때 등 다양한 시점에 적용 가능
* PointCut: JointPoint의 상세 스페ㄱ 정의. ex A라는 메서드의 진입 시점에 호출 <-처럼 더 구체적으로 advice 실행 지점 정할 수 있음

aspect실행 시점 annotation

* @Before (이전) : 어드바이스 타겟 메소드가 호출되기 전에 어드바이스 기능을 수행
* @After (이후) : 타겟 메소드의 결과에 관계없이(즉 성공, 예외 관계없이) 타겟 메소드가 완료 되면 어드바이스 기능을 수행
* @AfterReturning (정상적 반환 이후)타겟 메소드가 성공적으로 결과값을 반환 후에 어드바이스 기능을 수행
* @AfterThrowing (예외 발생 이후) : 타겟 메소드가 수행 중 예외를 던지게 되면 어드바이스 기능을 수행
* @Around (메소드 실행 전후) : 어드바이스가 타겟 메소드를 감싸서 타겟 메소드 호출전과 후에 어드바이스 기능을 수행


출처: https://engkimbs.tistory.com/746 [새로비]



https://docs.spring.io/spring-integration/docs/5.3.0.RC1/reference/html/mqtt.html


### /users

| 동작 | 요청 | Method | 기능 |
|:-------|:-------|:-------|:-------|
| 로그인 화면 | /users/login | GET | 로그인 화면을 띄운다 |
| 회원가입 화면 | /users/join | GET | 회원가입 화면을 띄운다 |
| 로그인 체크 | /users/login | POST | 로그인 시도 시 아이디 비밀번호 체크하고 로그인 한다 |
| 회원가입 하기 | /users/join | POST | 회원가입 한다 |
| 로그아웃 | /users/logout | GET | 로그하웃 한다 |
| 내 정보 | /users/{id} | GET | 로그인 된 아이디의 정보를 띄운다 |
| 내 정보 수정 | /users/{id} | PUT | 로그인 된 아이디의 정보를 수정한다 |
| 내 정보 삭제 | /users/{id} | DELETE | 로그인 된 아이디 정보를 삭제한다(탈퇴) |
| 가입 승인(보류) | /users/{b_id} | PUT | 보호자의 가입을 담당자가 승인한다 |


### /devices

| 동작 | 요청 | Method | 기능 |
|:-------|:-------|:-------|:-------|
| 기기 목록 | /devices | GET | 담당자가 관리하는 기기 목록을 띄운다 보호자인 경우 등록된 기기 하나를 띄운다 |
| 기기 등록 | /devices/{id} | POST |  |


GET /users/login : 로그인 화면
POST /users/login : 로그인 처리, 아이디 비밀번호 체크
GET /users/join : 회원가입 화면
POST /users/join : 회원가입 처리
GET /users/logout : 로그아웃 처리
GET /users/{uid} : {uid}(로그인 된 사람)의 개인정보 즉 내정보
----------------------------------------------------------20200615(오류처리x)
PUT /users/{uid} : 내 정보 수정
DELETE /users/{uid} : 회원 탈퇴 처리
PUT /users/{bid} : {bid}(보호자) 가입 신청 승인 처리

GET /devices : 관리하는 기기(노인정보 포함) 전체 리스트
POST /devices : 기기 등록
GET /devices/{num} : {num}기기 등록된 노인정보 상세정보 보기
PUT /devices/{num} : {num}기기 등록된 노인정보 상세정보 수정
DELETE /devices/{num} : {num}기기 삭제



<select id = "selectDevices" parameterType = "String" resultType = "com.spring.elderlycare.dto.DeviceUserDTO">
	SELECT dname, dtel, daddr FROM deviceUser
	WHERE staff=#{value} OR relative=#{value};
	</select>




# AJAX Asynchronous Javascript And Xml
### javaScript를 사용한 비동기 통신, 클라이언트와 서버간의 XML데이터 주고받는 기술(개발 기법)

https://coding-factory.tistory.com/143
http://tcpschool.com/ajax/intro

웹페이지 전체를 다시 로딩하는 대신 웹페이지의 일부분만 갱신
json, xml, html, 텍스트 파일 등 다양한 데이터 주고받을 수 있다.

장
1. 웹페이지 속도 향상
2. 서버의 처리 기다리지 않고 처리 가능
3. 서버에서 data만 전송

단
1. 히스토리 관리 안 됨
2. 연속 데이터 요청은 서버 부하 증가
3. XMLHttpRequest를 통해 통신 시, 요청 완료 전에 사용자가 페이지 떠나거나 오작동 가능성

ajax 프레임워크

* prototype
* script.aculo.us
* dojo
* jQuery
 등이 있다.


기존의 웹은 브라우저에서 httpRequest를 서버에 보내고 html및 css데이터를 받아서 웹 페이지 전체를 다시 로딩했다면,
ajax를 사용할때는 XMLHttpRequest를 보내고 서버에서는 ajax요청을 처리해서 HTML, XML, JSON데이터를 보내고 브라우저측에서 웹페이지의 일부분을 로딩한다.

1. 사용자의 요청 이벤트 발생
2. 요청 이벤트가 발생 시 이벤트 핸들러에 의해 자바스크립트 호출
3. 자바스크립트는 XMLHttpRequest객체를 사용하여 서버로 요청을 보냄 (보낸 후 응답을 기다리지 않고 다른 작업을 할 수 있다.)
4. 서버는 XMLHttpRequest를 받아서 Ajax요청을 처리
5. 서버는 처리한 결과를 html, xml, json의 형태로 웹 브라우저에 전달
6. 전달받은 데이터를 갱신하는 자바스크립트
7. 일부분 다시 로딩됨

XMLHttpRequest인스턴스 생성
```js
var httpRequest;

function createRequest() {

    if (window.XMLHttpRequest) { // 익스플로러 7과 그 이상의 버전, 크롬, 파이어폭스, 사파리, 오페라 등

        return new XMLHttpRequest();

    } else {                     // 익스플로러 6과 그 이하의 버전

        return new ActiveXObject("Microsoft.XMLHTTP");

    }

}
```

익스플로러 6이하 버전 사용자 거의 없으므로 아래꺼 사용
```js
var httpRequest = new XMLHttpRequest();
```

open() ajax요청 형식
```js
open(전달방식, URL주소, 동기여부);
```
open의 세번제 인자를 true로 전달하면 비동기식 요청을 보낼 수 있다. 서버로부터 응답을 기다리는 동안 다른 일을 할 수 있게 되는 것이다.
만약 false를 전달하면 서버로부터 응답이 올 때까지 어떤 다른 작업도 할 수 없다.

send() 작성된 ajax요청 서버로 전달
```js
send();       // GET 방식
send(문자열); // POST 방식
```

GET 요청
```js
httpRequest.open("GET", "/examples/media/request_ajax.php?city=Seoul&zipcode=06141", true);
httpRequest.send();
```

서버상의 문서 존재 유무
```js
if (httpRequest.readyState == XMLHttpRequest.DONE && httpRequest.status == 200 ) {

    ...

}
```
XMLHttpRequest.DONE : 서버에 요청한 데이터의 처리 완료, 응답 할 준비 됨
status프로퍼티 값이 200 : 요청한 문서가 서버상에 존재

POST 요청
```js
// POST 방식의 요청은 데이터를 Http 헤더에 포함시켜 전송함.

httpRequest.open("POST", "/examples/media/request_ajax.php", true);
httpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
httpRequest.send("city=Seoul&zipcode=06141");
```

서버로부터 응답 대기, 프로퍼티들
* readyState
* status
* onreadstatechange

readyStatus
1. UNSENT
2. OPENED
3. HEADERS_RECEIVED
4. LOADING
5. DONE

status
* 200 : 서버에 문서 존재
* 404 : 서버에 문서 존재하지 않음

onreadystatechange
readyState프로퍼티의 값이 변할 때마다 자동으로 호출되는 함수 설정, 총 5번 호출

```js
switch (httpRequest.readyState) {

    case XMLHttpRequest.UNSET:

        currentState += "현재 XMLHttpRequest 객체의 상태는 UNSET 입니다.<br>";

        break;

    case XMLHttpRequest.OPENED:

        currentState += "현재 XMLHttpRequest 객체의 상태는 OPENED 입니다.<br>";

        break;

    case XMLHttpRequest.HEADERS_RECIEVED:

        currentState += "현재 XMLHttpRequest 객체의 상태는 HEADERS_RECEIVED 입니다.<br>";

        break;

    case XMLHttpRequest.LOADING:

        currentState += "현재 XMLHttpRequest 객체의 상태는 LOADING 입니다.<br>";

        break;

    case XMLHttpRequest.DONE:

        currentState += "현재 XMLHttpRequest 객체의 상태는 DONE 입니다.<br>";

        break;

}

document.getElementById("status").innerHTML = currentState;

if (httpRequest.readyState == XMLHttpRequest.DONE && httpRequest.status == 200 ) {

    document.getElementById("text").innerHTML = httpRequest.responseText;

}
```


### http header
예제
```
Accept: */*

Referer: http://codingsam.com/examples/tryit/tryhtml.php?filename=ajax_header_request_01

Accept-Language: ko-KR

Accept-Encoding: gzip, deflate

User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko

Host: codingsam.com

DNT: 1

Connection: Keep-Alive
```

`setRequestHeader()`메소드 사용하여 HTTP요청 헤더 작성
```js
XMLHttpRequest인스턴스.setRequestHeader(헤더이름, 헤더값);
```

예제
```js
var httpRequest = new XMLHttpRequest();

httpRequest.onreadystatechange = function() {
    if (httpRequest.readyState == XMLHttpRequest.DONE && httpRequest.status == 200 ) {
        document.getElementById("text").innerHTML = httpRequest.responseText;
    }
};

httpRequest.open("GET", "/examples/media/ajax_request_header.php", true);

/************************/
httpRequest.setRequestHeader("testheader", "123");
/************************/

httpRequest.send();
```



(생략)




Ajax, JSON


users/login
성공 : {result: true, uid : (uid)}
실패 : {result: false, uid : undefined}

users/join
성공 : true
실패 : false

devices/




WARNING: An illegal reflective access operation has occurred

WARNING: Illegal reflective access by org.apache.ibatis.reflection.Reflector (file:/D:/1elderlyproject/web/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/elderlyCareSystem/WEB-INF/lib/mybatis-3.4.6.jar) to field java.lang.Boolean.value

WARNING: Please consider reporting this to the maintainers of org.apache.ibatis.reflection.Reflector

WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations

WARNING: All illegal access operations will be denied in a future release
login



### 6/19
* ajax success error 함수 실행 안 됨
* member delete member modify 만들기 일단 버튼은 홈 화면에.
* 가입 승인 만들기
* 서비스단 만들기


DELETE PUT 메소드 사용하기 위해 web.xml에 등록

```xml
<!-- HTTP Method Filter -->
<filter>
    <filter-name>httpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>httpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

1. 회원가입 : 관리자는 DB에 들어있다고 가정. 보호자 가입 기능. 노인의 번호를 입력해야 됨.
	* 노인의 key를 입력해줄지, 이름과 생년월일을 입력할지.
	* 가입승인은 관리자가 해줌. 가입승인 구현시 DB table에 저장하고 로그인 할 때, 받아와서 알려줘야 함
	* DB에서는 user, manage를 갱신한다. manage rel에 이미 들어있는 값이 있다면? 처리방안

2. 폼 (로그인, 회원가입, 기기등록, 정보수정) : 현재는 로그인폼도 컨트롤러에서 부름. 그렇게 구성하는 것이 맞는지.. 아닌거같음
	* GET users/login -> x GET users
	* POST users/login ->POST users/login
	* GET users/join -> x GET users
	* POST users/join -> POST users
	* 위처럼 수정할 수 있을까?
	* PUT users/{uid} 이거 위한 폼은?
	* jsp 하나로 만든 담에 조건따라 include로 합치기? 조건 어떻게 확인?

3. 기기 삭제 테스트 해봐야 됨
	* delete cascade 조건 줬지만 테스트 안해봤음

4. DB에 비밀번호 그대로 저장 노노


## Transaction 트랜잭션

https://www.youtube.com/watch?v=jSNrGgHk-ds
https://goddaehee.tistory.com/167

논리적 단위로 한 부분의 작업이 완료되었더라도 다른 부분의 작업이 완료되지 않을 경우 전체 취소되는 것. 작업 완료는 커밋(commit) 이라고 하고 작업 취소는 롤백(rollback)이라고 한다.

* 원자성 : 한 트랜잭션 내에서 실행한 작업은 하나로 간주
* 일관성 : 일관성있는 ㄷㅂ 상태 유지
* 격리성 : 동시에 실행되는 트랜잭션 영향 미치지 않도록 관리
* 지속성 : 트랜잭션 결과 저장

@Transactional <<트랜잭션 기능이 적용된 프록시 객체 생성됨
이 프록시 계체는 해당 어노테이션이 포함된 메소드 호출 시 `PlatformTransactionManager`를 사용하여 트랜잭션을 시작하고 정상여부에 따라 Commit또는 Rollback한다.



`<aop:aspectj-autoproxy></aop:aspectj-autoproxy>`
root-context.xml

웹에서 데이터 처리

김대업. "Client/Server 기반 원격 제어를 위한 실시간 모니터링 시스템 설계 및 구현." 국내석사학위논문 부경대학교대학원, 2002. 부산

길영준. "다중 생체신호를 이용한 혈압 추정 모델 및 IPv6 기반의 실시간 모니터링 시스템 개발." 국내박사학위논문 부산대학교, 2013. 부산

조덕연. "임베디드 리눅스를 이용한 산업용 제어기의 웹 모니터링." 국내석사학위논문 선문대학교, 2002. 충청남도

박제창. "만성 당뇨 환자의 자가 혈당 관리를 위한 지능형 헬스케어 시스템." 국내석사학위논문 강원대학교 일반대학원, 2019. 강원도

최정민. "작물 생육 환경 모니터링을 위한 비동기 IoT 브로커 설계 및 구현." 국내석사학위논문 인천대학교 정보기술대학원, 2017. 인천


```sql
create table realtimedata(
	measuredtime timestamp default current_timestamp on update current_timestamp,
    humid float,
    temp float,
    gas boolean
    );
    ```



# MQTT

1. 웹 서버에서 계속 돌아야 한다. (서버 시작부터 서버 실행중에는 계속)
2. 여러개 필요( 기기 수 만큼)


https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core

The `org.springframework.context.ApplicationContext` interface represents the Spring IoC container and is responsible for instantiating, configuring, and assembling the beans. The container gets its instructions on what objects to instantiate, configure, and assemble by reading configuration metadata. The configuration metadata is represented in XML, Java annotations, or Java code. It lets you express the objects that compose your application and the rich interdependencies between those objects.

Several implementations of the `ApplicationContext` interface are supplied with Spring. In stand-alone applications, it is common to create an instance of `ClassPathXmlApplicationContext` or `FileSystemXmlApplicationContext`. While XML has been the traditional format for defining configuration metadata, you can instruct the container to use Java annotations or code as the metadata format by providing a small amount of XML configuration to declaratively enable support for these additional metadata formats.

...

	XML-based metadata is not the only allowed form of configuration metadata. The Spring IoC container itself is totally decoupled from the format in which this configuration metadata is actually written. These days, many developers choose Java-based configuration for their Spring applications.

## Java-based Container Configuration
https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-java

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```
```xml
<beans>
    <bean id="myService" class="com.acme.services.MyServiceImpl"/>
</beans>
```
같은 코드.

Spring xml파일을 입력으로 사용하는 것과 거의 같은 방식으로 클래스를 인스턴스화 할 때 `ClassPathXmlApplicationContext`를 사용할 수 있다.
`@Configuration` `AnnotationConfigApplicationContext` 
```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```


https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling

The Spring Framework provides abstractions for the asynchronous execution and scheduling of tasks with the `TaskExecutor` and `TaskScheduler` interfaces, respectively.




### 20200625

INFO : org.springframework.web.servlet.DispatcherServlet - Initializing Servlet 'appServlet'
INFO : org.springframework.context.support.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'executor' of type [org.springframework.core.task.SimpleAsyncTaskExecutor] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
INFO : org.springframework.context.support.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'appConfig' of type [com.spring.elderlycare.util.AppConfig$$EnhancerBySpringCGLIB$$c330023] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
WARN : org.springframework.web.context.support.XmlWebApplicationContext - Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'mvcContentNegotiationManager': Initialization of bean failed; nested exception is java.lang.NoSuchMethodError: 'boolean org.springframework.core.annotation.AnnotationUtils.isCandidateClass(java.lang.Class, java.lang.Class)'
ERROR: org.springframework.web.servlet.DispatcherServlet - Context initialization failed
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'mvcContentNegotiationManager': Initialization of bean failed; nested exception is java.lang.NoSuchMethodError: 'boolean org.springframework.core.annotation.AnnotationUtils.isCandidateClass(java.lang.Class, java.lang.Class)'


월 25, 2020 7:20:00 오후 org.apache.catalina.core.ApplicationContext log
SEVERE: StandardWrapper.Throwable
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'mvcContentNegotiationManager': Initialization of bean failed; nested exception is java.lang.NoSuchMethodError: 'boolean org.springframework.core.annotation.AnnotationUtils.isCandidateClass(java.lang.Class, java.lang.Class)'



6월 25, 2020 7:20:00 오후 org.apache.catalina.core.StandardContext loadOnStartup
SEVERE: 웹 애플리케이션 [/elderlycare] 내의 서블릿 [appServlet]이(가) load() 예외를 발생시켰습니다.
java.lang.NoSuchMethodError: 'boolean org.springframework.core.annotation.AnnotationUtils.isCandidateClass(java.lang.Class, java.lang.Class)'



일단 mqtt는 직접 활성화 시킨다! 버튼을 만든다!
근데 만약에 서버를 재시작하는 경우
서버에서 알아서 전부 세팅해줘야 하는데 그건 나중에 구혀ㅛㄴ


mqtt connection lost.
localhost로는 정상 작동


테스트용. 스레드 엑스 20200626



publish 성공/ 라즈베리 파이

INFO : com.spring.elderlycare.controller.DeviceController - mqtt-thread : 0.0.0.0
INFO : com.spring.elderlycare.util.MQTTSubscriber - tcp://222.106.22.114:1883
======mqtt async test========
INFO : com.spring.elderlycare.util.MQTTSubscriber - connection lost
INFO : com.spring.elderlycare.util.MQTTSubscriber - publish Message
INFO : com.spring.elderlycare.util.MQTTSubscriber - delivery completed
근데 왜 connection lost?

publish 성공/ localhost

INFO : com.spring.elderlycare.controller.DeviceController - mqtt-thread : 0.0.0.0
INFO : com.spring.elderlycare.util.MQTTSubscriber - tcp://127.0.0.1:1883
======mqtt async test========
INFO : com.spring.elderlycare.util.MQTTSubscriber - publish Message
INFO : com.spring.elderlycare.util.MQTTSubscriber - delivery completed




Turns out, there was an EOFException that was being caused by our multiple clients having the same Client ID.

MQTT Brokers prematurely close any connections that are open with a Client ID if another connection with the same Client ID comes in.


https://github.com/eclipse/paho.mqtt.java/issues/207

https://github.com/eclipse/paho.mqtt.java/blob/master/org.eclipse.paho.client.mqttv3/src/main/java/org/eclipse/paho/client/mqttv3/MqttAsyncClient.java#L1266

MqttAsyncClient 사용
https://gist.github.com/benedekh/697b3507e0b3f890f105
```java
package mqtt.demo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscribeSample implements MqttCallback {

    public static void main(String[] args) {
        String topic = "MQTT Examples";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "JavaAsyncSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(new MqttSubscribeSample());
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Thread.sleep(1000);
            sampleClient.subscribe(topic, qos);
            System.out.println("Subscribed");
        } catch (Exception me) {
            if (me instanceof MqttException) {
                System.out.println("reason " + ((MqttException) me).getReasonCode());
            }
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void connectionLost(Throwable arg0) {
        System.err.println("connection lost");

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.err.println("delivery complete");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        System.out.println("message: " + new String(message.getPayload()));
    }
    
}
```



reason 32104
msg 클라이언트가 연결되지 않음
INFO : com.spring.elderlycare.util.MqttSubscriber2 - connection lost
loc 클라이언트가 연결되지 않음
cause null
excep 클라이언트가 연결되지 않음 (32104)
클라이언트가 연결되지 않음 (32104)
	at org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(ExceptionHelper.java:31)


단순 mqtt 코드를 이용하였을 때, localhost에서는 오류가 없었으나 외부 네트워크의 broker로 접근 할 때, connection lost 발생. 검색해보니 deadlock이 원인이라는 말도 있음. 비동기 mqtt 객체가 있다고 하여 소스코드 확인 후 해당 객체로 구현. 제대로 동작함.
callback함수인 messagearrived내부에 dto에 접근하는 코드 넣으면 또 connection lost 발생
MqttException (0) - java.lang.NullPointerException
오류 발생.

INFO : com.spring.elderlycare.util.MqttSubscriber2 - MqttException (0) - java.lang.NullPointerException
MqttException (0) - java.lang.NullPointerException
	at org.eclipse.paho.client.mqttv3.internal.CommsCallback.run(CommsCallback.java:176)
	at java.base/java.lang.Thread.run(Thread.java:832)
Caused by: java.lang.NullPointerException
	at com.spring.elderlycare.util.MqttSubscriber2.messageProcessing(MqttSubscriber2.java:93)
	at com.spring.elderlycare.util.MqttSubscriber2.messageArrived(MqttSubscriber2.java:81)
	at org.eclipse.paho.client.mqttv3.internal.CommsCallback.handleMessage(CommsCallback.java:354)
	at org.eclipse.paho.client.mqttv3.internal.CommsCallback.run(CommsCallback.java:162)
	... 1 more


MqttException (0) - java.lang.NullPointerException
at org.eclipse.paho.client.mqttv3.internal.CommsCallback.run(CommsCallback.java:176)


```java
	public void run() {
		final String methodName = "run";
		callbackThread = Thread.currentThread();
		callbackThread.setName(threadName);
		
		synchronized (lifecycle) {
			current_state = State.RUNNING;
		}

		while (isRunning()) {
			try {
				// If no work is currently available, then wait until there is some...
				try {
					synchronized (workAvailable) { //<<<<오류 나는 부분... 왜?
						if (isRunning() && messageQueue.isEmpty()
								&& completeQueue.isEmpty()) {
							// @TRACE 704=wait for workAvailable
							log.fine(CLASS_NAME, methodName, "704");
							workAvailable.wait();
						}
					}
				} catch (InterruptedException e) {
				}
				(생략)
```
```java
if (mqttCallback != null || callbacks.size() > 0) {
```
여기가ㅏ 오류 나는 부분인 것 같음 mqttCallback이 null이 되ㄴ는듯

20200701
```java
private void insertData(String topic, MqttMessage message) {
		try { 
			String tmp = topic.split("/")[1];
			
			if(tmp.equals("humid")||tmp.equals("temp")) {
				Map<String, Object> obj = new HashMap<String, Object>();
				float data = Float.parseFloat(message.toString()); 
				obj.put("elderly", eld);
				obj.put(tmp, data);
				sqlSession.insert(ns+"log", obj);
			}else
				alertToApp(tmp);
			
			
		  }catch(NumberFormatException e) { 
			  //"home/vid"
			  
		  }
	}
```
바로 db에 접근하도록 코드를 수정해보았다.
`sqlSession.insert(ns+"log", obj);`부분에서 같은 오류 발생.


### 20200702


1. 웹, MQTT-외부 프로젝트로 따로 구현함- 붙이기. 웹 서버 실행시 MQTT프로그램 실행되도록.
2. 비밀번호 SHA256암호화 코드 
3. 프론트엔드 전체 (데이터 그래프)
4. rest api data async polling
5. 비디오 받고 decoding, storage저장, db에는 파일이름 등으로 저장
6. 이상상황 push 알림

<br>
dto 스트링으로
home/vid는 새벽 4시에 옴.
마지막거 하나
message비어있으면 없는거
<br>
그래프는 프론트에서 해야되는 것 같음
내일 웹 mqtt붙이기 해야될듯. 비밀번호 암호화도
그 담에 프론트 시작
<<<<<<< HEAD

### 20200703

## json and ajax
https://www.youtube.com/watch?v=rJesac0_Ftw

```js
var ourRequest = new XMLHttpReqeust();
ourRequest.open('GET','json경로');
ourRequest.onload = function(){
	//data가 load됐을때 무엇을 할 것인가.
	var ourData = JSON.parse(ourRequest.responseText);
	console.log(ourData[0]);
}; 
ourRequest.send();
```

Asynchronous (in the background, not required page re?)
JavaScript
And
XML (JSON)

`XMLHttpRequest`




`<button id = "btn">`

```js
var container = document.getElementById("info");
var btn = document.getElementById("btn");
var pageCounter = 1; //바뀌는 값.

btn.addEventListener("click", function(){
	var ourRequest = new XMLHttpReqeust();
	ourRequest.open('GET','json경로'+ pageCounter+'.json');
	ourRequest.onload = function(){
		//data가 load됐을때 무엇을 할 것인가.
		var ourData = JSON.parse(ourRequest.responseText);
		//console.log(ourData[0]);
		renderHTML(ourData);
		pageCounter++;
		if(pageCounter>3)
			btn.classList.add("hide-me");
	}; 
ourRequest.send();
});

function renderHTML(data){
	var htmlString = "";

	for(i = 0; i < data.length; i++){
		htmlString +="<p>"+data[i].name +" is a "+ data[i].species+".</p>"
	}

	container.insertAdjacentHTML("beforeend", htmlString);
}
```



생활코딩
https://opentutorials.org/course/53/50
```
${'.info'}.html('test');//??
```
```js
//(생략)
function clickHandler(event){
	var nav = document.getElementById('naviation');
	for(var i = 0; i < nav.childNodes.length; i++){
		if(child.nodeType == 3)
			continue;
		child.className ='';
	}
	event.target.className = 'selected';
}

addEvent(window, 'load', function(eventObj){
	var nav = document.getElementById('navigation');
	for(var i = 0; i < nav.childNodes.length; i++){
		var child = nev.childNodes[i];
		if(child.nodeType == 3)
			continue;
		addEvent(child, 'click', clickHandler);
	}
});
//생략
```
--------->
```js
${'#navigation li'}.live('click', function(){
	${'#navigation li'}.removeClass("selected");
	$(this).addClass("selected");
})
```

### 오늘 한 일
* 비밀번호 암호화
* 로그인 시 권한 받아서 세션에 저장
* 프론트 공부 조금
=======
>>>>>>> cab01cc4f124b3604d6fb363582c4ab9fcde2ae0



### 20200706

@EventListener

```java
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupHousekeeper {

  @EventListener(ContextRefreshedEvent.class)
  public void contextRefreshedEvent() {
    // do whatever you need here 
  }
}
```
https://www.it-swarm.dev/ko/java/spring-%EC%8B%9C%EC%9E%91%EC%8B%9C-%EB%A9%94%EC%86%8C%EB%93%9C-%EC%8B%A4%ED%96%89/968099933/

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/event/EventListener.html

가장 나은 방법 사용

* init-method = "..." 사용

* InitializingBean 인터페이스 구현 -> ApplicationContext에 배포되면 bean 생성 시 afterPropertiesSet() 메서드 호출

* @PostConstruct Bean에 메소드에 주석 달기. Bean 작성 시 주석 달린 메소드 호출

* Spring 라이프사이클에 묶일 인프라 Bean이 많으면 ApplicationListener<ContextRefreshedEvent>를 구현.
onApplicationEvent(..)메소드는 Spring 시작되는 동안 호출

<bean name = "starter" init-mehtod = "start" class = "com.example.StartBean" lazy = "false" />

https://www.it-swarm.dev/ko/java/spring-mvc%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EC%8B%9C%EC%9E%91%EC%8B%9C-java-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%8B%A4%ED%96%89/972322251/

빈에 등록하는 방법 써봤는데 여전히 sqlSession.어쩌구 호출부에서 Nullpointerexception오류 남. 외부 실행 프로젝트로 만들어서 불러오는 방법 써봐야지
코드 먼저 다듬기

외부 실행파일 실행하는법
Runtime


바로 db에 입력이 안 되고 \


데이터는 10초마다 주는데 몰아서 들어감.


WARN : org.springframework.web.servlet.PageNotFound - No mapping for GET /elderlycare/resource/jquery-3.5.1.js
WARN : org.springframework.web.servlet.PageNotFound - No mapping for GET /elderlycare/jquery-3.5.1.js

The method getContextpath() is undefined for the type HttpServletRequest

<script type = "text/javascript" src = "${pageContext.request.contextpath}/resources/jquery-3.5.1.js"></script>


### 20200707

한 일
7. 3. 로그인 시 권한 받기, 프론트 다듬기, 비밀번호 암호화
7. 6. 서버 실행 시 mqtt 실행 되도록 하기 //db에 바로 안 들어가짐

할 일
*
7. 7. mqtt connection lost시 reconnect 시도. background mqtt db에 바로 insert. 홈 화면에 device목록 띄우기. 새로운 기기 등록 시 connect.
7. 8. 대시보드 플랫폼 정하기. device 목록에서 device 정보 버튼 클릭 정보 띄우기. 로그아웃 홈으로 리다이렉트. 회원가입 성공 시 홈으로 리다이렉트 실패시 실패 alert
7. 9 - 7. 10. 안드로이드에서 데이터 받기 rest api polling? 공부

**
7. 13. 안드 데이터 받기. 보호자 가입 승인 기능. 
7. 14. 안드 데이터 받기. home/{num}/video 받아 폴더에 영상 저장하기.
7. 15. 안드 데이터 받기. home/{num}/video 받아 폴더에 저장한 영상 정보 db에 저장하기.
7. 16. 안드 데이터 받기. data 보여주기 화면 구성. 현재 온습도, 맥박 출력하기
7. 17. 안드 데이터 받기. 온습도 그래프. GPS 지도

**
7. 20. 화면 동작 체크.
7. 21 -. push 알림.


오류 수정
WARN : org.springframework.web.servlet.PageNotFound - No mapping for GET /elderlycare/resource/jquery-3.5.1.js

root-context.xml에 추가
<mvc:resources mapping = "/js/**" location = "/resources/js/"></mvc:resources>

경로 수정
<script type = "text/javascript" src = "<c:url value = '/resources/js/jquery-3.5.1.js'/>"></script>


1. reconnect callback으로 부른다. reconnect시 subscribe 필요.


reconnect 못함ㅜㅜ
내일 reconnect랑 db 넣는거. 오전에는 화면 구성 먼저 다 해놓고 오후에 mqtt 끝내기

20200708

오전 : 
홈 화면에 device목록 띄우기.
대시보드 플랫폼 정하기.
device 목록에서 device 정보 버튼 클릭 정보 띄우기.
로그아웃 홈으로 리다이렉트.
회원가입 성공 시 홈으로 리다이렉트 실패시 실패 alert


jquery 경로
$(location).attr('pathname'); /elderlycare/
$(location).attr('host'); localhost:9090
$(location).attr('hostname'); localhost
$(location).attr('href'); http://localhost:9090/elderlycare/


오후 : 
mqtt reconnect, mqtt data store into db. < 안되면 마지막으로 미루기
오늘 온습도 불러오기 갑자기 안 됨.
프론트 추가 공부


reconnect
`options.setAutomaticReconnect(true);` 설정 하면 reconnect는 됨.
MqttCallbackExtended implements 한 후, `connectComplete()` 에서 `subscribe()`호출 시 connection lost 발생함. null pointer exception.

실행파일로 만들기
* 프로젝트 우클릭
* export
* runnable jar file
* java -jar [파일]


오늘 발견한 오류
서버가 종료돼도 mqtt가 종료 안됨.
근데 종료 안 되는게 맞는 것 같긴 함.
근데 모르겠음.

그럼 저걸 따로 돌려야 되나.
spring에 붙일 필요가 없었나

spring 실행 시 시작되도록 하면
event 발생 시 - 기기등록 - disconnect - 재구동? 아니면 메인에서 옵션 줘서 나눠? 하나만 추가로 구동시키는 옵션 줘서 이벤트 발생시 
Runtime.getRuntime().exec("java -jar "+mqttProcess+" \"[ip번호]\"");
이런 식으로?

따로 구동하는 게 나을 것 같다는 결론.
나중에 보완


### 20200709

* 안드로이드에서 데이터 받기! mqtt 

스프링 MVC 비동기 처리 https://12bme.tistory.com/565

비동기 처리 방법
* 컨트롤러 핸들러 메서드에서 callable타입 반환
* WebAsyncTask 타입 반환

web.xml 설정에서 활성화 해야 쓸 수 있음
```xml
<servlet>
  <!-- ASYNC -->
  <async-supported>true</async-supported>
</servlet>

<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>

    <!-- ASYNC -->
    <async-supported>true</async-supported>

    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>

    <!-- ASYNC -->
    <dispatcher>ASYNC</dispatcher>
</filter-mapping>
```

스프링 MVC에서 비동기 요청 처리를 사용하ㅕㄹ면 필터/서블릿 등록시 `setAsyncSupported()`메서드 호출.
WebApplicationInitializer
```java
public class MWebApplicationInitializer implements WebApplicationInitializer{
	@Override
	public void onStartup(ServletContext ctx){
		DispatcherServlet servlet = new DispatcherServlet();
		ServletRegistration.Dynamic registration = ctx.addServlet("dispatcher", servlet);
		registration.setAsyncSupported(true);
	}
}
```

MVC구성 클래스에 설정
```java
@Configuration
public class AsyncConfiguration extends WebMvcConfiguratijonSupport{
	@Override
	protected void configurationAsyncSupport(AsyncSupportConfigurer configurer){
		configurer.setDefaultTimeout(5000);
		configurer.setTaskExecutor(mvcTaskExecutor());
	}
	@Bean
	public ThreadPoolTaskExecutor mvcTaskExecutor(){
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setThreadGroupName("mvc-executor");
		return taskExecutor;
	}
}
```

비동기 컨트롤러
: 핸들러 메서드의 반환형만 바꾸면 비동기 처리 컨트롤러로 사용 가능
* Callable
```java
@Controller
@RequestMapping("/exQuery")
public class ExQueryController{
	private final ExService exService;

	public ExServiceController(ExService exService){
		this.exService = exService;
	}

	@GetMapping
	public void setupForm(){}

	@PostMapping
	public Callable<String> submitForm(@RequestParam("name")String name, Model model){
		return()->{
			List<ex> list = java.util.Collection.emptyList();
			if(courtName != null){
				Delayer.randomDelay();
				list = exService.query(name);
			}
			model.addAttribute("list", ex);
			return "exQuery";
		};
	}
}
```
* DeferredResult
 클래스 인스턴스를 만들어 비동기 처리작업(Runnable)을 전송 후, setResult()메서드를 이용해 DeferredResult결괏값 설정
```java
@Controller
@RequestBody("/exQuery")
public class ExQueryController{
	private final ExService exService;
	private final TaskExecutor taskExecutor;

	public ExQueryCotroller(ExQueryService exQueryService, AsyncTaskExecutor taskExecutor){
		this.exService = exService;
		this.taskExecutor = taskExecutor;
	}

	@GetMapping
	public void setupForm(){}

	@PostMapping
	public DeferredResult<String> submitForm(@RequestParam("name")String name, Model model){
		final DeferredResult<String> result = new DeferredResult<>();

		taskExecutor.execute(()->{
			List<Ex> list = java.util.Collections.emptyList();
			if(name !=null){
				Delayer.randomDelay();
				list = exService.query(name);
			}
			model.addAttribute("list", ex);
			result.setResult("exQuery");
		});
		return result;
	}
}
```

mqttclient는 비동기임. 커ㄴ[ㄱ션  구독을 비동기로 함.
클라이언트 여러개 돌림 가능함
별개의 어플리케이션으로 실시간 db 적재 가능함
\스프링에서 부르면 db적재 안 됨. 기기가 하나인 경우 가능함.
혹시 mqtt starter자체를 비동기로 불러야 되는건지.
이따가 5시에 만들어보기.


데이터 오면 받아서 넣는 비동기 컨트롤러.


안드로이드쪽
```java
package com.example.mybtchat.data;

public class ElderlyData {
    private String elderlyName;
    private String elderlyStep;
    private String elderlyPulse;
    private String elderlyKcal;
    private String elderlyLatitude;
    private String elderlyLongitude;

    public ElderlyData(String name, String step, String pulse, String kcal, String latitude, String longitude){
        elderlyName = name;
        elderlyStep = step;
        elderlyPulse = pulse;
        elderlyKcal = kcal;
        elderlyLatitude = latitude;
        elderlyLongitude = longitude;
    }

    public String getElderlyName() { return elderlyName; }

    public void setElderlyName(String elderlyName) { this.elderlyName = elderlyName; }

    public String getElderlyStep() { return elderlyStep; }

    public void setElderlyStep(String elderlyStep) { this.elderlyStep = elderlyStep; }

    public String getElderlyPulse() { return elderlyPulse; }

    public void setElderlyPulse(String elderlyPulse) { this.elderlyPulse = elderlyPulse; }

    public String getElderlyKcal() { return elderlyKcal; }

    public void setElderlyKcal(String elderlyKcal) { this.elderlyKcal = elderlyKcal; }

    public String getElderlyLatitude() { return elderlyLatitude; }

    public void setElderlyLatitude(String elderlyLatitude) { this.elderlyLatitude = elderlyLatitude; }

    public String getElderlyLongitude() { return elderlyLongitude; }

    public void setElderlyLongitude(String elderlyLongitude) { this.elderlyLongitude = elderlyLongitude; }
}
```
*
7. 9. 비동기 데이터 받기. 밴드 데이터 보내기. mqtt
7. 10. 오전 - rest 전체 구성도 확인 naming 점검 보완 / 오후 - 7.9와 동일


**
7. 13. 보호자 가입 승인. home cctv 실시간 스트리밍(homeiot:8090/?action=stream)
7. 14. home/{num}/video 받아 폴더에 영상 저장하기. home/{num}/video 받아 폴더에 저장한 영상 정보 db에 저장하기.
7. 15. data 보여주기 화면 구성. 현재 온습도, 맥박 출력하기. 온습도 그래프. GPS 지도
7. 16 - 7. 17. 화면 동작 체크. 오류 수정. 필요 기능 추가




내일 와서 할 거:
이거 읽어보고 수정
```
WARN : org.springframework.web.context.request.async.WebAsyncManager - 
!!!
An Executor is required to handle java.util.concurrent.Callable return values.
Please, configure a TaskExecutor in the MVC config under "async support".
The SimpleAsyncTaskExecutor currently in use is not suitable under load.
-------------------------------
Request URI: '/elderlycare/datas/1'
!!!
```

mqtt starter async로 돌려보기.
실시간 db 넣는거 async 문제인거 같음.
https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling-task-executor-types
You can not use @Async in conjunction with lifecycle callbacks such as @PostConstruct. To asynchronously initialize Spring beans, you currently have to use a separate initializing Spring bean that then invokes the @Async annotated method on the target, as the following example shows:
```java
public class SampleBeanImpl implements SampleBean {

    @Async
    void doSomething() {
        // ...
    }

}

public class SampleBeanInitializer {

    private final SampleBean bean;

    public SampleBeanInitializer(SampleBean bean) {
        this.bean = bean;
    }

    @PostConstruct
    public void initialize() {
        bean.doSomething();
    }

}
```


### 20200710

@async종료하기.
https://stackoverflow.com/questions/38880069/spring-cancel-async-task

destroy-method로
```java
public void mqttdestroy() {
		future.cancel(true);
		try {
			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
```
잉거 등록했는데 disconnect 안됨. 근데 disconnect 안 해도 될 거 ㅅ같기도 함.
만약 reconnect 기능 생기면?
일단은 서버 꺼져도 계속 mqtt 데이터 적재됨.


```
WARN : org.springframework.web.context.request.async.WebAsyncManager - 
!!!
An Executor is required to handle java.util.concurrent.Callable return values.
Please, configure a TaskExecutor in the MVC config under "async support".
The SimpleAsyncTaskExecutor currently in use is not suitable under load.
-------------------------------
Request URI: '/elderlycare/datas/1'
!!!
```
 위 오류 simpleasynctaskexecutor등록했던거 지우고 아래 threadpooltaskexecutor 등록함.

```xml
<bean id = "taskExecutor" class = "org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor" >
		<property name = "corePoolSize" value = "5" />
		<property name = "maxPoolSize" value = "10"/>
		<property name = "queueCapacity" value = "25"/>
	</bean>	
```

참고로 코드로는 이렇게
```java
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Override
  public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
      configurer.setDefaultTimeout(5000);
      // == 스레드풀을 이용하도록 커스터마이징한 TaskExecutor를 설정 ==
      configurer.setTaskExecutor(mvcTaskExecutor());
  }

  @Bean
  public TaskExecutor mvcTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(25);
    return executor;
  }
}
```

mqtt async로 했는데도 데이터 넣는거 안 됨.

안드에서 데이터 주고받기 테스트 해봐야 됨.



**
7. 13. mqtt db입력 오류 해결. mqtt reconnect 구현. 안드로이드에서 json 데이터 받기 테스트.
7. 14. 보호자 가입 승인. home cctv 실시간 스트리밍(homeiot:8090/?action=stream)
7. 15. home/{num}/video 받아 폴더에 영상 저장하기. home/{num}/video 받아 폴더에 저장한 영상 정보 db에 저장하기.
7. 16. data view 화면 구상. 현재 정보(온습도, 맥박, 걸음). 오늘 정보 그래프(온습도, 맥박, 걸음). GPS 지도. 이상 데이터 범위 설정, 관리하는 노인 목록에 표시. 등등
7. 17. 못 끝낸 거 추가 작업. 화면 동작 체크. 오류 수정. 필요 기능 추가.


### 20200713

**
7. 13. mqtt db입력 오류 해결. mqtt reconnect 구현. 안드로이드에서 json 데이터 받기 테스트.
7. 14. 보호자 가입 승인. home cctv 실시간 스트리밍(homeiot:8090/?action=stream)
7. 15. home/{num}/video 받아 폴더에 영상 저장하기. home/{num}/video 받아 폴더에 저장한 영상 정보 db에 저장하기.
7. 16. data view 화면 구상. 현재 정보(온습도, 맥박, 걸음). 오늘 정보 그래프(온습도, 맥박, 걸음). GPS 지도. 이상 데이터 범위 설정, 관리하는 노인 목록에 표시. 등등
7. 17. 못 끝낸 거 추가 작업. 화면 동작 체크. 오류 수정. 필요 기능 추가.



mqtt db입력 오류 해결. mqtt reconnect 구현. 안드로이드에서 json 데이터 받기 테스트.

mqtt reconnect 구현. 이건 나중에

가입승인:
보호자 - 
회원가입 시, 노인의 이름과 생년월일을 입력받는다.
user table insert.
manage table update.

관리자 -
로그인 시, manage하는 device list에서 manage의 relative중 role이 -1인 사람 리스트를 가져옴
리스트를 보여줌. 옆에 가입 승인 버튼
승인 버튼 누르면 role++;

spring, mybatis, jpa, herinate?


### 20200714
보호자 가입 승인. home cctv 실시간 스트리밍(homeiot:8090/?action=stream)

```sql
UPDATE manage SET relative = "ddd" 
	WHERE elderly = (
	SELECT ekey FROM elderly 
	WHERE ename = "노인1" AND ebirth = "19330202");
```
```sql
UPDATE manage SET relative = "ddd" 
	WHERE elderly = (
	SELECT ekey FROM elderly 
	WHERE ename = "노인1" AND ebirth = "19330201");
```

Transaction

Atomicity
Consistency
Isolation
Durability
세로 읽기 ACID : 대충 힘들다는 뜻

@Transactional
```xml
<!-- 트랜잭션 관련 설정 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource"></property>
</bean>
	
<!-- 트랜잭션 어노테이션 인식 -->
<tx:annotation-driven/>
```
<<이거 했는데 왜 안되냐

그냥 직접 롤백 하는거 시도

트랜잭션, deletecascade 없애기


**
7. 15. 오전 - join 부분 수정. db 수정. / 오후 - home cctv 실시간 스트리밍(homeiot:8090/?action=stream)
7. 16. home/{num}/video 받아 폴더에 영상 저장하기. home/{num}/video 받아 폴더에 저장한 영상 정보 db에 저장하기.
7. 17. data view 화면 구상. 현재 정보(온습도, 맥박, 걸음). 오늘 정보 그래프(온습도, 맥박, 걸음). GPS 지도. 이상 데이터 범위 설정, 관리하는 노인 목록에 표시. 등등

+ 트랜잭션ㅜㅜ

iframe

기기 등록 시, mqtt

동영상 저장.

파일 업로드
```java
File targetFile = new File("/경로"+multipartFile.getOriginalFilename());
try{
	InputStream fileStream = multipartFile.getInputStream();
	FileUtils.copyInputStreamToFile(fileStream, targetFile);
}catch(IOException e){
	FileUtils.deleteQuietly(targetFile);
	e.printStackTrace();
}
return "redirect:/";
```

### 20200716

**
7. 16. home/{num}/video 받아 폴더에 영상 저장하기. home/{num}/video 받아 폴더에 저장한 영상 정보 db에 저장하기.
7. 17. data view 화면 구상. 현재 정보(온습도, 맥박, 걸음). 오늘 정보 그래프(온습도, 맥박, 걸음). GPS 지도. 이상 데이터 범위 설정, 관리하는 노인 목록에 표시. 등등

encoding 된 byte[] 를 mqttmessage로 받음. 
message.toString
byte string to byte array
decoding
decoding byte to file
file save



DATA에 온습도 추가할거면
DTO 새로 만들기
db는 그대로 나눠둠


root-context.xml
datasource bean

```sql
select * FROM(select * FROM banddata
where ekey = #{value}
order by measuredtime DESC limit 1)b
cross join (SELECT humid, temp FROM realtimedata
where elderly = #{value}
order by measuredtime DESC limit 1)a;
```


Error Code: 1248. Every derived table must have its own alias

b
```
select * FROM(select * FROM banddata
where ekey = #{value}
order by measuredtime DESC limit 1)b
```


datas2dto에 humid temp 추가 안하고 map으로 전달할 지
그냥 할 지

list로 받을때는 따로 주니까
그건 또 어떻게 하지
일단 냅두고

내일은 프론트부터 만들어야겠다


20200717


https://www.egrappler.com/templatevamp-twitter-bootstrap-admin-template-now-available/



<script type = "text/javascript" src = "<c:url value = '/resources/js/jquery-3.5.1.js'/>"></script>

<script type = "text/javascript">
$(document).ready(function(){
	
	$('#btn-logout').click(function(){
		$.getJSON('/elderlycare/users/logout', function(data){
			window.location.replace('');
		});
	});
});

</script>





**
7.20. 전체 화면 틀 만들기
7.21 - 7.22. 현재 데이터, 데이터 그래프 만들기.
7.23. 기기 목록, 현재 상태
7.24. 로그인, 로그아웃, 회원가입, 기기등록.

20200720

<!DOCTYPE html5>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page session="true" %>
<html>
<head>
	<title>Home</title>

</head>
<body>
<%@ include file="header.jsp" %>

<h1>
	Hello world!  
</h1>
<p>session id : <%=session.getAttribute("uid") %></p>
<p>test ${uid }</p>
<P>  The time on the server is ${serverTime}. </P>
<div id = "list">
</div>

</body>

<script type = "text/javascript" src = "<c:url value = '/resources/js/jquery-3.5.1.js'/>"></script>
<script type = "text/javascript">
 	$(document).ready(function(){
		var html = '';
		$.getJSON('devices', function(data){
			$.each(data, function(index, item){
				html+='<p>';
				html+=item.ename+', '+item.ebirth+', '+item.etel+', '+item.eaddr;
				//html += '<button type = \"button\" onClick = \"location.href=\'http://'; //현재창
				html += '<button type = \"button\" onClick = \"window.open(\'http://';	//새창
				html +=item.homeIoT;
				html +=':8090/?action=stream\')\">';
				html +=item.homeIoT+'</button>'
				
				//data 보기 button
				
				html+='</p>';
				
			});
			$('div').html(html);
		});
		/*$(데이터 보기 버튼).click(function(){
			포워드 , 데이터  화면
		});*/
		
	});
	 
</script>

</html>



Spring mvc에서 js, css 파일 사용하기

root-context.xml
```xml
<mvc:resource mapping = "/resources/**" location = "/resources/" />
```
/resource/js, /resource/css와 같은 경로를 명시하는 경우에 /resources/ 경로로 해석하겠다는 의미. resources는 webapp/resources를 의미한다.

```
<c:url value = "/resources/css/main.css" />" rel = "stylesheet">
```
이런 식으로 사용 가능.

a태그에 js function 사용하기.
`<a href="javascript:함수();">`
url에 자동으로 #이 붙는ㄴ 경우가 생김

`<a href = "#" onClick=함수();return false;>`
	로 해결

```js
$(document).ready(function(){
	
	$('#btn-logout').click(function(){
		$.getJSON('/elderlycare/users/logout', function(data){
			window.location.replace('');
		});
	});
});
```










<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page session="true" %>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<link href="<c:url value = '/resources/css/bootstrap.min.css'/>" rel="stylesheet">
<link href="<c:url value = '/resources/css/bootstrap-responsive.min.css'/>" rel="stylesheet">
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
        rel="stylesheet">
<link href="<c:url value = '/resources/css/font-awesome.css'/>" rel="stylesheet">
<link href="<c:url value = '/resources/css/style.css'/>" rel="stylesheet">
<link href="<c:url value = '/resources/css/pages/dashboard.css'/>" rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body>
<c:set var = "contextPath" value = "<%=request.getContextPath() %>"></c:set>

<a href = "${contextPath}/">HOME</a>
<c:if test = "${empty uid }">
	<a href = "${contextPath}/users/login" class = "btn btn-default" role = "button">로그인</a>
	<a href = "${contextPath}/users/join" class = "btn btn-default" role = "button">회원가입</a>
</c:if>
<c:if test = "${not empty uid }">
	<a href = "${contextPath}/users/info" class = "btn btn-default" role = "button">내 정보</a>
	<a class = "btn btn-default" id = "btn-logout" role = "button">로그아웃</a>
	<c:if test = "${auth == 1 }">
	<a href = "${contextPath}/devices/form" class = "btn btn-default" role = "button">기기등록</a>
	<a href  = "" class = "btn btn-default" role = "button">가입 승인</a>
	</c:if>
	<br/>
	<form class = 'delete-form' action = "${contextPath}/users/info" method = "post">
	<input type = "hidden" name = "_method" value = "delete"/>
	<button type = "submit">회원 탈퇴</button>
	</form>
	<a href = "${contextPath}/users/mod-form" class = "btn btn-default" role = "button">정보 수정</a>
	

</c:if>

</body>
<script type = "text/javascript" src = "<c:url value = '/resources/js/jquery-3.5.1.js'/>"></script>

<script type = "text/javascript">
$(document).ready(function(){
	
	$('#btn-logout').click(function(){
		$.getJSON('/elderlycare/users/logout', function(data){
			window.location.replace('');
		});
	});
});

</script>

</html>



자바스크립트에서 context path 사용하기

<script type = "text/javascript" charset="utf-8">
	sessionStorage.setItem("contextpath", "${pageContext.request.contextPath}");
</script>
처음에 세션에 저장해두고 필요할때
var ctx = sessionStorage.getItem("contextpath");


<tr>
                    <td> 노인1 </td>
                    <td> 주소주소 주소 주소 주소주소</td>
                    <td> 0313333333</td>
                    <td class="td-actions"><a href="javascript:;" class="btn btn-small btn-success"><i class="btn-icon-only icon-ok"> </i></a><a href="javascript:;" class="btn btn-danger btn-small"><i class="btn-icon-only icon-remove"> </i></a></td>
                  </tr>




 <mvc:annotation-driven ignoreDefaultModelOnRedirect="true" />





**
7.21 - 7.22. 현재 데이터, 데이터 그래프 만들기.
7.23. 현재 상태
7.24. 로그인, 로그아웃, 회원가입, 기기등록.



헤더 분리 or 합체?????????????



20200721




<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>
```html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
<%@ include file="../header.jsp" %>
<h1>login page</h1>
<form action = "login" id ="login-form" method = "POST">
<div>
	<label for = "uid">ID:</label>
	<input id = "uid" type = "text" name = "uid" >
	</div>
	<div>
	<label for = "upwd">PASSWORD:</label>
	<input id = "upwd" type = "password" name = "upwd" >
</div>
<button type = "submit">로그인</button>
</form>
</body>
<script type = "text/javascript" src = "/elderlycare/resources/jquery-3.5.1.js"></script>
<script type = "text/javascript">
$(function(){
	$('#login-form').submit(function(event){
		event.preventDefault();
		
		//var data = {"uid": "staff101058", "upwd": "staff101058"};
		var data = {uid: $("#uid").val(), upwd: $('#upwd').val()};

		$.ajax({
				type : 'POST',                            
				url : 'login',                        
				dataType : 'json',                          
				contentType : 'application/json',            
				data : JSON.stringify(data),            
				success : function(response){
					if(response.result){
						alert(response.uid+'님 환영합니다.');
						$(location).attr("href", "${contextPath}/");
					}else
						alert("아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
				},                      
				error   : function(response){
					alert(response.uid);
				}
		});
	});
});
	


</script>
</html>
```

```js
var key = ${edto.ekey}
		//alert(key);
		//html = "<div class ='stat'><i class = 'icon-asterisk'></i><span class = 'value'>33</span></div>";
		//$('#test').html(html);
		var html = '';
     	 $.getJSON(key+'/curdata', function(data){
     		alert(data.temp);
     		//$.each(data, function(index, item){
     			html += "<div class='stat'> <i class='icon-asterisk'></i> <span class='value'>";
     			html+= data.temp;
     			html+= "</span> </div>";
     			
     			html += "<div class='stat'> <i class='icon-tint'></i> <span class='value'>";
	     		html+= data.humid;
	     		html+= "</span> </div>";
	     			
	     		html += "<div class='stat'> <i class='icon-heart'></i> <span class='value'>";
		     	html+= data.epulse;
		     	html+= "</span> </div>";
		     		
		     	html += "<div class='stat'> <i class='icon-shopping-cart'></i> <span class='value'>";
			    html+= data.estep;
			    html+= "</span> </div>";
     		//});
     		$('#cur-data').html(html); 
     	}); 
```
이렇게 통째로 넣으려니까 안 됨 왜 그런지 모르겠음

```
<div id="big_stats" class="cf" id = "cur-data">
                  
                      
                    <div class="stat"> <i class="icon-asterisk"></i> <span class="value" id = "temp"></span> </div>
                
                    
                    <div class="stat"> <i class="icon-tint"></i> <span class="value"id = humid></span> </div>
             
                    
                    <div class="stat"> <i class="icon-heart"></i> <span class="value"id = "epulse"></span> </div>
              
                    
                    <div class="stat"> <i class="icon-shopping-cart"></i> <span class="value"id = "estep"></span> </div>
               
                </div>
```
```js
var key = ${edto.ekey}
		//alert(key);
		//html = "<div class ='stat'><i class = 'icon-asterisk'></i><span class = 'value'>33</span></div>";
		//$('#test').html(html);
		var html = '';
     	 $.getJSON(key+'/curdata', function(data){
     		
     		//$.each(data, function(index, item){
     			//html += "<div class='stat'> <i class='icon-asterisk'></i> <span class='value'>";
     			html= data.temp;
     			//html+= "</span> </div>";
     			$('#temp').html(html);
     			
     			//html += "<div class='stat'> <i class='icon-tint'></i> <span class='value'>";
	     		html= data.humid;
	     		//html+= "</span> </div>";
	     		$('#humid').html(html);
	     			
	     		//html += "<div class='stat'> <i class='icon-heart'></i> <span class='value'>";
		     	html= data.epulse;
		     	//html+= "</span> </div>";
		     	$('#epulse').html(html);
		     		
		     	//html += "<div class='stat'> <i class='icon-shopping-cart'></i> <span class='value'>";
			    html= data.estep;
			    //html+= "</span> </div>";
			    $('#estep').html(html);
     		//});
     		//$('#cur-data').html(html);
     	});
```

하나씩 넣어줬음




그래프!!!!!!!!
이상 데이터 처리!!!!!!!!!!!
실시간 스트리밍!!!!!!
녹화비디오!!!!!!


20200722

** 데이터 받을때 범위 많이 벗어나는 예외 데이터 지우기.



7.23. 맥박 그래프, 걸음 수 그래프, 삐죽 튀어나오는 데이터들 거르기(mqtt)

7.24. 현재 상태 (stat) 이상 있을때 목록에 체크표시, 회원가입, 기기등록, 동영상


20200723

```json
{
	labels: 
	["17:45","17:45","17:45","17:45","17:45","17:44","17:44","17:44","17:44","17:44","17:44","17:43","17:43","17:43","17:43","17:43","17:42","17:42","17:42","17:42"],

	datasets: 
	{
		fillColor: "rgba(151,187,205,0.5)",
		strokeColor: "rgba(151,187,205,1)",
		data: [76,62,74,68,67,72,66,74,72,67,79,65,74,68,67,72,63,79,65,74]
	}
}
```



24***** 급!!
1. 걸음 수 그래프
2. 동영상 스트리밍
3. gps
4. stat 0이라면 데이터를 보낸다. rest로 받아서 바로 화면 처리한다 실시간 처리.
5. 2일 움직임 없으면 mqtt -> //alone 이걸 어떻게 처리하지...


안 급
1. home elderly list 크기 키우기 or 글씨 줄이기.
2. chart current status 에서 개인정보 적기.

20200724


javascript에서 json을 sessionStorage에 저장하려고 했더니 오류
`Uncaught TypeError: Cannot read property 'ekey' of null`

json을 string형식으로 만들어 저장
사용시에 json 변환
```javascript
sessionStroage.setItem("elderly", JSON.stringify(elderly));
var eld = JSON.parse(sessionStorage.getItem("elderly"));
```


```
function selectDev(key, dev){
		sessionStorage.setItem("ekey", key);
		sessionStorage.setItem("dev", dev);
		window.location.replace('');
	}
	 $(document).ready(function() {
	 	var html = '';
	 	$.getJSON('/elderlycare/devices', function(data){
	 		$.each(data, function(index, item){
	 			//html +="<li><a href = 'devices/"+item.ekey+"'>";
	 			var param = JSON.stringify(item)
	 			console.log(param);
	 			html+="<li><a href='#' onclick='selectDev("+item.ekey+", \""+item.homeIoT.toString() +"\"); return false;'>"
	 			html +=item.ename+"</a></li>";
	 			console.log(html);
	 		});
	 		$('#eld-list').html(html);
	 	});
```

```
var dev = sessionStorage.getItem("dev");
	
	console.log("ekey:"+sessionStorage.getItem("ekey"));
	console.log("add:"+dev);
	console.log(sessionStorage["iot"])
	var url = "http://"+ipaddr+":8090/?action=stream";
```

저게 안 보내져서 2-3시간 삽질함.
지금은 잘 보내짐 왜그런지 모르겠음 아마 오타인 것 같음.



```js
var html = '<iframe src ="'+url+'" width="325" height="240">이 브라우저는 iframe을 지원하지 않습니다.</iframe>';
	console.log(html);
	$('#streaming').html(html);
```

`jquery-1.7.2.min.js:4 Resource interpreted as Document but transferred with MIME type multipart/x-mixed-replace: "http://121.138.83.121:8090/?action=stream".`
오류



3. gps
4. stat 0이라면 데이터를 보낸다. rest로 받아서 바로 화면 처리한다 실시간 처리.
5. 2일 움직임 없으면 mqtt -> //alone 이걸 어떻게 처리하지...

안 급
1. home elderly list 크기 키우기 or 글씨 줄이기.
2. chart current status 에서 개인정보 적기.


카카오 지도 api
https://apis.map.kakao.com/web/sample/basicMap/
app key 발급받기


20200727

A cookie associated with a cross-site resource at http://jquery.com/ was set without the `SameSite` attribute. A future release of Chrome will only deliver cookies with cross-site requests if they are set with `SameSite=None` and `Secure`. You can review cookies in developer tools under Application>Storage>Cookies and see more details at https://www.chromestatus.com/feature/5088147346030592 and https://www.chromestatus.com/feature/5633521622188032.

Indicate whether to send a cookie in a cross-site request by specifying its SameSite attribute

sessionStorage 문제.
홈에서 리스트 선택 후 모니터링 창으로 왔을때 저장 x
모니터링 창에서 리스트 선택 후 로그아웃시 sessionStorage 저장 되어 있음
1. 홈에서 리스트 선택 시 ip 정보 저장.
2. 로그아웃시 ip 정보 삭제

ekey 같은 경우 1번은 제대로 작동
로그아웃시 정보 삭제는 되지 않음. function logout에 sessionStorage.remove()쓰면 될 것 ㅏㄱㅌㄴ음

27 gps, stat
28 stat
29 mqtt alone
30 31 전체 점검

구글맵 (유료) 무료 평가판 사용.
https://developers.google.com/maps/documentation/javascript/get-api-key?hl=ko

### SSE Server-Sent Events
폴링처럼 주기적인 요청은 쓸모없는 요청으로 인한 낭비. HTTP오버헤드
sse는 서버가 필요할 때, 클라이언트에게 데이터를 줄 수 있게 해주는 서버 푸시 기술

* HTTP통신
* 간편함

SseEmitter클래스를 사용하여 http응답을 text/event-stream 타입으로 리턴

```xml
<servlet>
	<servlet-name>community-servlet</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/servlet-config/community-servlet-context.xml
        </param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
</servlet>
```
async-support를 반드시 추가해줘야 한다.

이슈를 저장하는 서비스 단에서는 이슈 저장이 완료되면 EventPublisher를 사용하여 이벤트를 발생시킨다.


elderly dto 에 stat 추가


key 별로 가장 최근의 데이터 가져오기
```sql
select * from(
    select * from banddata
    where (ekey, measuredtime)in(
		select ekey, max(measuredtime) as measuredtime
		from banddata group by ekey
    )
    order by measuredtime desc
)t;
```


20200728

alone alert! ipc
Inter Process Communication
프로세스들이 공유하는 메모리가 필요하다!


* pipe
통신을 위한 별도의 메모리 공간(버퍼)를 할당해주어야 한다. 단방향 통신이다.

Anonymouse pipe - 외부 프로세스에서 파이프 사용 불가. 부모프로세스가 자식 프로세스를 생성하는 경우 사용. 단방향 통신( pipe)

named pipe - 외부 프로세스와 통신을 위해. 반이중통신. 전이중 통신을 위해서는 읽기용 쓰기용 두가지 파이프를 생성해줘야 한다. 다수의 클라이언트를 처리하기에 비효율적 (mkfifo, mknod)



1. device/null/curdata 400 error 
	: alert '사용하지 않는 기기이거나 선택된 리스트가 없습니다. list 목록에서 기기선택을 먼저 해주세요'
2. header class


20200729

기기등록시 mqtt 서비스는 어떻게..?
mqtt 기능 합체 할 지...



 cookie associated with a cross-site resource at http://jquery.com/ was set without the `SameSite` attribute. A future release of Chrome will only deliver cookies with cross-site requests if they are set with `SameSite=None` and `Secure`. You can review cookies in developer tools under Application>Storage>Cookies and see more details at https://www.chromestatus.com/feature/5088147346030592 and https://www.chromestatus.com/feature/5633521622188032.


 20200730 
 mqtt 코드 합치기
 (home/{num}/alone) 처리는??


 List<DevicesDTO> list = service.getIPList();

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'MqttStarter' defined in ServletContext resource [/WEB-INF/spring/root-context.xml]: Invocation of init method failed; nested exception is java.lang.NullPointerException


1. device/null/curdata 400 error 
	: alert '사용하지 않는 기기이거나 선택된 리스트가 없습니다. list 목록에서 기기선택을 먼저 해주세요'

회원가입 기기등록시 빈칸 오류화면 말고 alert

온습도를 5초 간격으로 받아봤는데 데이터가 600개 정도에서는 버벅거리다가 한번에 쌓이고 천개부터는 안 쌓임. 
왜 그러는지 모르겠음 내일 테스트

스케쥴러로 DB realtime 데이터 밤 12시에 지우기

회원가입 기기등록 안됨.
오류화면 처리(회원가입 기기등록시 빈칸, num == null)
mqtt video 저장
mqtt alone 메세지
스케쥴러 데이터 지우기
graph 가장 최근 데이터 측정 시간 보여주기


```xml
<task:annotation-driven executor="myExecutor" scheduler="myScheduler" />
<task:executor id="myExecutor" pool-size="5" />
<task:scheduler id="myScheduler" pool-size="5" />
<bean id="jobService" class="com.spschool.scheduler.Scheduler" />
```

`@Scheduled(fixedDelay = 5000)`, `@Scheduled(cron="* * * * * *")`


20200803

POST: datas 
	- param: datas 

GET: users/login
POST: users/login
	- param: uid, upwd(, regid) / return: 관리자 로그인(uid, result)
GET: users/join
POST: users/join
GET: users/logout

GET: devices
	-param: uid / return: 담당 노인 정보 리스트
GET: devices/datas
GET: devices/monitoring
GET: devices/{ekey}
	-return: {ekey} 노인 정보
GET: devices/{ekey}/banddatas 
	-return: {ekey} 노인 최근 banddata 20개
GET: devices/{num}/htdatas 
	-return: {ekey} 노인 오늘 하루 ht data
GET: devices/{num}/curdata
	-{ekey} 노인 최근 데이터 band & ht


POST: devices/login
	-param : ename, ebirth / return : ekey, homeIoT(, regId)



관리자 login시 regId 저장하기.


approvalForm



20200805

* 담당 직원은 회원가입 과정 필요 없이 등록 되어 있다고 가정
* home monitoring기기는 미리 식별번호(해당 프로그램에서는 ekey)를 가지고 있다고 가정
* 일반 user(보호자)의 경우 가입 후 가입 승인 시, 노인의 정보 확인 가능
* 보호자는 한 명만 등록 가능

서버
1. 오류 화면
2. 트랜잭션
3. 세션.... -> 로그인 없이 url로 접근 가능한 것이 몇 개 있음 이거 aop?

화면

1. 담당직원

	1-1. 로그인 화면
	1-2. 메인화면
	1-3. datas 화면
	1-4. home monitoring 화면
	1-5. 기기등록 화면
	1-6. 가입 승인 화면
	1-7. profile - 보호자와 노인 정보 출력

2. 보호자 (승인)

	2-1. 로그인 화면
	2-2. 메인화면
	2-3. datas 화면
	2-4. home monitoring 화면
	2-5. 회원가입 화면
	2-6. profile - 담당자와 노인 정보 출력

3. 보호자 (미승인)

	3-1. 로그인 후 home 화면에서 alert("승인되지 않은 가입자 입니다.")




https://walbatrossw.github.io/spring-mvc/2018/07/24/17-spring-mvc-board-remember-me.html

function(start, end, allDay) {
    var title = prompt('Event Title:');
    if (title) {
       	calendar.fullCalendar('renderEvent',
        {
           	title: title,
           	start: start,
           	end: end,
           	allDay: allDay
        },
        true // make the event "stick"
    	);
    }
    calendar.fullCalendar('unselect');
}




내일 할 일 : calendar, 걸음 수 dao에서 직접 계산... 


Resource interpreted as Stylesheet but transferred with MIME type text/html: "<URL>".
	잘되는거 확인하고 껐는데 켰더니'' 오류남 
	이클립스 몇번 껐다가 켰더니 다시 됨 너무 화가 남



20200807

Date.toISOString() 함수는 ISO 8601에 기반한 24 혹은 27 길이의 날짜/시각 문자열을 반환하는 함수인데,

timezone이 항상 zero UTC offset이다.

timezone을 반영하고 싶으면 아래 코드 참고.


출처: https://bloodguy.tistory.com/entry/JavaScript-DatetoISOString-timezone-offset-반영 [Bloodguy]


20200811 
error page 설정

web.xml
```xml
<error-page>
	<location>/errors</location>
</error-page>
```

controller
```java
@Controller
public class ErrorController {

	@RequestMapping(value = "errors", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        
        ModelAndView errorPage = new ModelAndView("errorPage");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);
 
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }
    
    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
          .getAttribute("javax.servlet.error.status_code");
    }
}

```

```
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h1>${errorMsg}</h1>
</body>
</html>
```

20200812

인터셉터 등록시 jquery 인식 못하는 듯 함 왜?
