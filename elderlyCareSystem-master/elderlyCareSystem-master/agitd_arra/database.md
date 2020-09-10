# health monitoring database
```sql
create database healthmonitoring;

use healthmonitoring;
```
## member table
보호자와 담당자가 있다. 담당자는 가입할 필요 없이 등록되어있다고 가정한다.<br>
보호자는 가입요청을 할 수 있고, 담당자가 승인하면 그 때부터 사용 가능하다.
```sql
create table membe(
	m_key int NOT NULL AUTO_INCREMENT,
    m_id varchar(32) NOT NULL,
    m_pwd varchar(32) NOT NULL,
    m_name varchar(16) NOT NULL,	
    m_tel varchar(16) NOT NULL,
    m_email varchar(64) NOT NULL,	#-1:승인되지 않은 가입자 0:승인된 가입자 1:담당자
    m_role int NOT NULL,
    PRIMARY KEY(m_key)
);
```
## device table
기기에는 기기와 기기사용자의 정보를 저장한다.<br>
# 통신 위한 정보 추가 필요 20200604<br>

```sql
create table device(
	d_key int NOT NULL AUTO_INCREMENT,
    d_name varchar(32) NOT NULL,
    d_birth date NOT NULL,
    d_tel varchar(16),
    d_address varchar(256),
    d_illness varchar(128),			#질병코드를 ';'로 구분하여 작성.
    PRIMARY KEY(d_key)
);
```
## relation table
관계 정리 테이블
```sql 
create table manage_relation(
	device int,
    staff int,
    guardian int,
	FOREIGN KEY (device) REFERENCES device(d_key),
    FOREIGN KEY (staff) REFERENCES membe(m_key),
    FOREIGN KEY (guardian) REFERENCES membe(m_key)
);
```

추가로 만들 테이블
datalog_today

datalog_day
datalog_month



-------------
#DB connection


```java

    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String URL = "jdbc:mysql://127.0.0.1:3306/healthmonitoring";
    static final String USERNAME = "healthmonitoring";
    static final String PASSWORD = "hm1234";

```
DB 연결 테스트를 하는데 오류가 발생했다.
```
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary. java.sql.SQLException: The server time zone value '????α? ????' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support. at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129)
```


https://developer-kylee.tistory.com/8<br>
위의 블로그 참고하여 수정
```java

    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String URL = "jdbc:mysql://127.0.0.1:3306/healthmonitoring?serverTimezone=UTC";
    static final String USERNAME = "healthmonitoring";
    static final String PASSWORD = "hm1234";

```




### DB수정
20200611
```sql
create table `user`(
    uid varchar(32) NOT NULL,
    upwd varchar(32) NOT NULL,
    uname varchar(16) NOT NULL, 
    utel varchar(16) NOT NULL,
    uemail varchar(32) NOT NULL,    
    urole int NOT NULL,             #-1:승인되지 않은 가입자 0:승인된 가입자 1:담당자
    PRIMARY KEY(uid)
);

create table deviceUser(
    dkey int NOT NULL AUTO_INCREMENT,
    dname varchar(32) NOT NULL,
    dbirth date NOT NULL,
    dtel varchar(16),
    daddr varchar(64),
    PRIMARY KEY(dkey)
);
drop table device;
create table device(
    dkey int,
    staff varchar(32),
    relative varchar(32),
    FOREIGN KEY (dkey) REFERENCES deviceUser(dkey),
    FOREIGN KEY (staff) REFERENCES `user`(uid),
    FOREIGN KEY (relative) REFERENCES `user`(uid),
    homeIoT varchar(32),
    bandIoT varchar(32),
    PRIMARY KEY(dkey)
);
```
{"uid":"staff101058","upwd":"staff101058","uname":"김영희","utel":"01011112222","uemail":"young@fakeemail.com","urole":1}

db 합침
```sql

drop table device;
drop table deviceUser;



create table deviceUser(
    dkey int NOT NULL AUTO_INCREMENT,
    dname varchar(32) NOT NULL,
    dbirth date NOT NULL,
    dtel varchar(16),
    daddr varchar(64),
    staff varchar(32),
    relative varchar(32),
    FOREIGN KEY (staff) REFERENCES `user`(uid),
    FOREIGN KEY (relative) REFERENCES `user`(uid),
    homeIoT varchar(32),
    bandIoT varchar(32),
    PRIMARY KEY(dkey)
);
```

test user input
```sql
insert into `user`(uid, upwd,uname, utel, uemail, urole)
values("staff101058", "staff101058", "김영희", "01011112222", "young@fakeemail.com", "1");

insert into `user`(uid, upwd, uname, utel, uemail, urole)
values("staff101101", "staff101101", "최바둑", "01012342222", "alphago@fakeemail.com", "1");

insert into `user`(uid, upwd, uname, utel, uemail, urole)
values("staff101103", "staff101103", "고지식", "01000000000", "highknowledge@fakeemail.com", "1");
```

test deviceuser input
```sql
insert into deviceUser(dname, dbirth, dtel, daddr, staff, relative, homeIoT, bandIoT)
values("테스트1", "1930-01-01", "0311111111", "경기도 수원시 팔달구 123412341234 ㅇㅇ", "staff101058", null, "0.0.0.0", "0.0.0.0");


insert into deviceUser(dname, dbirth, dtel, daddr, staff, relative, homeIoT, bandIoT)
values("테스트2", "1930-01-02", "0312222222", "경기도 수원시 장안구 123412341234 ㅇㅇ", "staff101058", null, "0.0.0.0", "0.0.0.0");

insert into deviceUser(dname, dbirth, dtel, daddr, staff, relative, homeIoT, bandIoT)
values("테스트3", "1930-01-03", "0313333333", "경기도 수원시 영통구 123412341234 ㅇㅇ", "staff101058",null, "0.0.0.0", "0.0.0.0");

insert into deviceUser(dname, dbirth, dtel, daddr, staff, relative, homeIoT, bandIoT)
values("테스트4", "1930-01-04", "0314444444", "경기도 수원시 팔달구 123412341234 ㅇㅇ", "staff101058", null, "0.0.0.0", "0.0.0.0");
```

/users/devices
[{"dkey":0,"dname":"테스트1","dbirth":null,"dtel":"0311111111","daddr":"경기도 수원시 팔달구 123412341234 ㅇㅇ","staff":null,"relative":null,"homeIoT":null,"bandIoT":null},{"dkey":0,"dname":"테스트2","dbirth":null,"dtel":"0312222222","daddr":"경기도 수원시 장안구 123412341234 ㅇㅇ","staff":null,"relative":null,"homeIoT":null,"bandIoT":null},{"dkey":0,"dname":"테스트3","dbirth":null,"dtel":"0313333333","daddr":"경기도 수원시 영통구 123412341234 ㅇㅇ","staff":null,"relative":null,"homeIoT":null,"bandIoT":null},{"dkey":0,"dname":"테스트4","dbirth":null,"dtel":"0314444444","daddr":"경기도 수원시 팔달구 123412341234 ㅇㅇ","staff":null,"relative":null,"homeIoT":null,"bandIoT":null}]


```sql
create table `user`(
    uid varchar(32) NOT NULL,
    upwd varchar(32) NOT NULL,
    uname varchar(16) NOT NULL, 
    utel varchar(16) NOT NULL,
    uemail varchar(32) NOT NULL,    
    urole int NOT NULL,             #-1:승인되지 않은 가입자 0:승인된 가입자 1:담당자
    PRIMARY KEY(uid)
);

create table elderly(
    ekey int NOT NULL AUTO_INCREMENT,
    ename varchar(32) NOT NULL,
    ebirth date NOT NULL,
    etel varchar(16),
    eaddr varchar(64),
    PRIMARY KEY(dkey)
);

create table manage(
    elderly int NOT NULL,
    staff varchar(32) NOT NULL,
    relative varchar(32),
    FOREIGN KEY (elderly) REFERENCES elderly(ekey),
    FOREIGN KEY (staff) REFERENCES `user`(uid),
    FOREIGN KEY (relative) REFERENCES `user`(uid),
    PRIMARY KEY (elderly)
);

create table devices(
    elderly int NOT NULL,
    FOREIGN KEY(elderly) REFERENCES elderly(ekey),
    homeIoT varchar(32),
    bandIoT varchar(32),
    PRIMARY KEY(elderly)
);

```



```sql

alter table devices add constraint fk_constraint_1 foreign key(elderly) 
references elderly(ekey) on delete cascade;
alter table manage add constraint fk_constraint_2 foreign key(elderly) 
references elderly(ekey) on delete cascade;
```
manage의 staff는?




```sql
create table realtimedata(
    measuredtime timestamp default current_timestamp on update current_timestamp,
    humid float,
    temp float,
    gas boolean
    );
```



```sql
alter table realtimedata drop gas;
```

```sql

alter table `user` change upwd upwd varchar(256);

alter table manage add constraint fk_constraint_3 foreign key(staff) 
references `user`(uid) on delete cascade;
alter table manage add constraint fk_constraint_4 foreign key(relative) 
references `user`(uid) on delete cascade;
```

```sql
create table banddata(
    measuredtime timestamp default current_timestamp on update current_timestamp,
    ekey int,
    estep int,
    epulse int,
    ekcal double,
    ealtitude double,
    elongitude double,
    constraint fk_constraint_5 foreign key (ekey) references elderly(ekey) on delete cascade
);
```
```sql
alter table banddata add stat int;
```

```sql
alter table manage add regId varchar(128);
```