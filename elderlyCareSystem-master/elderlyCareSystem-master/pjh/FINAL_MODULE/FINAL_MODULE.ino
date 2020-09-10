#include "U8glib.h"                    // OLED Lib
#include <math.h>
#include <SPI.h>
#include <Wire.h>
#include <SoftwareSerial.h>
#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.
#include <PulseSensorPlayground.h>     // Includes the PulseSensorPlayground Library.


#define PULSE_SIG A2                    // PulseSensor GREEN WIRE connected to ANALOG PIN A1
#define THREASHOLD 550                 // Determine which Signal to "count as a beat" and which to ignore.
// Use the "Gettting Started Project" to fine-tune Threshold Value beyond default setting.
// Otherwise leave the default "550" value.

PulseSensorPlayground pulseSensor;            // Creates an instance of the PulseSensorPlayground object called "pulseSensor"
U8GLIB_SSD1306_128X64 u8g(13, 11, 10, 9, 8);  // SW SPI Com: SCK(D0) = 13, MOSI(D1) = 11, CS = 10, A0(DC) = 9, Res = 8

/* Bluetooth */
SoftwareSerial BTSerial(2, 3); //Connect HC-06. Use your (TX, RX) settings
SoftwareSerial gpsSerial(6, 5); //Connect GPS-V3-NEO. Use your (TX, RX) settings

char c = "";                // Wn 인지 구분 및 str에 저장.
String str = "";            // \n 전까지 c 값을 저장.
String targetStr = "GPGGA"; // str의 값이 NMEA의 GPGGA 값인지 타겟


/* time */
#define SENDING_INTERVAL 1000
#define SENSOR_READ_INTERVAL 50
unsigned long prevSensoredTime = 0;
unsigned long curSensoredTime = 0;
String  X = "0";
String  Y = "0";
String  Z = "0";

/* Data buffer */
#define ACCEL_BUFFER_COUNT 125
byte aAccelBuffer[ACCEL_BUFFER_COUNT];
int iAccelIndex = 2;

/* MPU-6050 sensor */
#define MPU6050_ACCEL_XOUT_H 0x3B     // R
#define MPU6050_PWR_MGMT_1 0x6B       // R/W
#define MPU6050_PWR_MGMT_2 0x6C       // R/W
#define MPU6050_WHO_AM_I 0x75         // R
#define MPU6050_I2C_ADDRESS 0x68

typedef union accel_t_gyro_union {
  struct {
    uint8_t x_accel_h;
    uint8_t x_accel_l;
    uint8_t y_accel_h;
    uint8_t y_accel_l;
    uint8_t z_accel_h;
    uint8_t z_accel_l;
    uint8_t t_h;
    uint8_t t_l;
    uint8_t x_gyro_h;
    uint8_t x_gyro_l;
    uint8_t y_gyro_h;
    uint8_t y_gyro_l;
    uint8_t z_gyro_h;
    uint8_t z_gyro_l;
  } reg;

  struct {
    int x_accel;
    int y_accel;
    int z_accel;
    int temperature;
    int x_gyro;
    int y_gyro;
    int z_gyro;
  } value;
};
uint8_t draw_state = 0;



char chConvert_Int[16];                       // 정수 -> 문자열 변환용 캐릭터형 변수 선언
bool bHeart_Status = false;                   // bool 변수 선언

/* EMERGENCY BUTTON START */
int sw = 12;       // 스위치(SW) 핀 설정
int led = 7;       // LED 핀 설정

int state = LOW;      // LED 상태
int reading;          // SW 상태
int previous = LOW;   // SW 이전 상태

long time = 0;        // LED가 ON/OFF 토글된 마지막 시간
long debounce = 100;  // Debounce 타임 설정
/* EMERGENCY BUTTON START END */

void setup()
{
  int error;
  uint8_t c;

  Serial.begin(9600);
  Serial.println("Start GPS... ");
  gpsSerial.begin(9600);
  Wire.begin();                     //Wire 라이브러리 초기화
  BTSerial.begin(9600);             // set the data rate for the BT port

  // default at power-up:
  // Gyro at 250 degrees second
  // Acceleration at 2g
  // Clock source at internal 8MHz
  // The device is in sleep mode.
  //
  error = MPU6050_read (MPU6050_WHO_AM_I, &c, 1);
  Serial.print(F("WHO_AM_I : "));
  Serial.print(c, HEX);
  Serial.print(F(", error = "));
  Serial.println(error, DEC);

  // According to the datasheet, the 'sleep' bit
  // should read a '1'. But I read a '0'.
  // That bit has to be cleared, since the sensor
  // is in sleep mode at power-up. Even if the
  // bit reads '0'.
  error = MPU6050_read (MPU6050_PWR_MGMT_2, &c, 1);
  Serial.print(F("PWR_MGMT_2 : "));
  Serial.print(c, HEX);
  Serial.print(F(", error = "));
  Serial.println(error, DEC);

  // Clear the 'sleep' bit to start the sensor.
  MPU6050_write_reg (MPU6050_PWR_MGMT_1, 0);

  initBuffer();

  // Configure the PulseSensor object, by assigning our variables to it.
  pulseSensor.analogInput(PULSE_SIG);
  pulseSensor.setThreshold(THREASHOLD);


  pulseSensor.begin();

  pinMode(sw, INPUT_PULLUP); // SW 를 설정, 아두이노 풀업저항 사용
  pinMode(led, OUTPUT);      // LED 설정
}

void loop() {

  if (gpsSerial.available()) // gps 센서 통신 가능?
  {
    c = gpsSerial.read();  // 센서의 값 읽기

    if (c == '\n') {       // \n 값인지 구분.

      if (targetStr.equals(str.substring(1, 6))) {   // \n 일시. 지금까지 저장된 str 값이 targetStr과 맞는지 구분

        Serial.println(str); // NMEA 의 GPGGA 값일시

        int first = str.indexOf(",");                // , 를 토큰으로서 파싱.
        int two = str.indexOf(",", first + 1);
        int three = str.indexOf(",", two + 1);
        int four = str.indexOf(",", three + 1);
        int five = str.indexOf(",", four + 1);
        String Lat = str.substring(two + 1, three);           // Lat과 Long 위치에 있는 값들을 index로 추출
        String Long = str.substring(four + 1, five);
        String Lat1 = Lat.substring(0, 2);                    // Lat의 앞값과 뒷값을 구분
        String Lat2 = Lat.substring(2);
        String Long1 = Long.substring(0, 3);                  // Long의 앞값과 뒷값을 구분
        String Long2 = Long.substring(3);
        double LatF = Lat1.toDouble() + Lat2.toDouble() / 60; // 좌표 계산.
        float LongF = Long1.toFloat() + Long2.toFloat() / 60;

        Serial.print("Lat : ");          // 좌표 출력.
        Serial.println(LatF, 15);
        Serial.print("Long : ");
        Serial.println(LongF, 15);
      }
      str = "";                          // str 값 초기화
    } else {                             // \n 아닐시, str에 문자를 계속 더하기
      str += c;
    }
  }
  /* MPU6050 FOR  STEP COUNT  */
  curSensoredTime = millis();

  // Read from sensor
  if (curSensoredTime - prevSensoredTime > SENSOR_READ_INTERVAL) {
    readFromSensor();  // Read from sensor
    prevSensoredTime = curSensoredTime;

    // Send buffer data to remote
    if (iAccelIndex >= ACCEL_BUFFER_COUNT - 3) {
      sendToRemote();
      initBuffer();
      Serial.println("------------- Send 20 accel data to remote");
    }
  }
  


  /*----------------------BPM SENSOR ---------------------------------------------------*/
  int iMy_BPM = pulseSensor.getBeatsPerMinute();  // Calls function on our pulseSensor object that returns BPM as an "int"."myBPM" hold this BPM value now.
  itoa(iMy_BPM, chConvert_Int, 10);               // Convert integer to string. 매개변수 (바꿀 정수값, 바뀐 문자열 저장 공간, 진법)

  if (pulseSensor.sawStartOfBeat())
  {
    bHeart_Status = !bHeart_Status;  // 반전
  }
  delay(20);
  u8g.firstPage();
  do
  {
    if (draw_state < 130) {
      draw_state++;
      Draw();
    }
    Draw();
  } while ( u8g.nextPage() );


  /* EMERGENCY BUTTON  */
  reading = digitalRead(sw);  // SW 상태 읽음


  if (reading == HIGH && previous == LOW && millis() - time > debounce) { //SW 가 눌려졌고 스위치 토글 눌림 경과시간이 Debounce 시간보다 크면 실행
    if (state == HIGH)    // LED 가 HIGH 면 LOW 로 바꿔준다.
      state = LOW;
    else                  // LED 가 LOW 면 HIGH 로 바꿔준다.
      state = HIGH;

    time = millis();
  }

  digitalWrite(led, state);
  previous = reading;

}
