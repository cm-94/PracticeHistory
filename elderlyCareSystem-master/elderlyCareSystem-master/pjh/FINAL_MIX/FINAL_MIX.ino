/*
 *완료한 것(~7/17)
 1.심박수 측정 데이터 OLED로 출력
 2.걸음수 측정을 위한 가속도센서 XYZ 값 OLED 로 출력 및 앱으로 송신
 3.HC-06 모듈을 이용 스마트폰 어플과 통신
 4.GPS 모듈을 이용해 위도,경도 파싱 
 5.각 센서의 소스코드를 합쳐 기능구현
 
 *해야 할 것(7/17~7/24)
 1.심박수 데이터 앱으로 송신
 2.앱으로부터 걸음수,칼로리 데이터 수신
 3.파싱한 위도,경도 값 앱으로 송신
 4.아두이노 프로미니를 이용해 스마트밴드 만들기
 5.스마트밴드와 앱의 데이터 송수신 점검
 
 * 플러스 @
 1.각 기능들 모듈화
 2.화면 깜박거림 제거
 3.걸음수 IMAGE 만들기
 */

#include "U8glib.h"                    // OLED Lib
#include <math.h>
#include <SPI.h>
#include <Wire.h>
#include <SoftwareSerial.h>
#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.
#include <PulseSensorPlayground.h>     // Includes the PulseSensorPlayground Library.

#define PULSE_SIG 1                    // PulseSensor PURPLE WIRE connected to ANALOG PIN 0
#define THREASHOLD 550                 // Determine which Signal to "count as a beat" and which to ignore.
                                       // Use the "Gettting Started Project" to fine-tune Threshold Value beyond default setting.
                                       // Otherwise leave the default "550" value. 
                                             
PulseSensorPlayground pulseSensor;            // Creates an instance of the PulseSensorPlayground object called "pulseSensor"
U8GLIB_SSD1306_128X64 u8g(13, 11, 10, 9, 8);  // SW SPI Com: SCK(D0) = 13, MOSI(D1) = 11, CS = 10, A0(DC) = 9, Res = 8
//U8GLIB_SH1106_128X64 u8g(13, 11, 10, 9, 8); // SW SPI Com: SCK = 13, MOSI = 11, CS = 10, A0 = 9
/* Bluetooth */
SoftwareSerial BTSerial(2, 3); //Connect HC-06. Use your (TX, RX) settings
SoftwareSerial gpsSerial(6,5);
char c = "";  // Wn 인지 구분 및 str에 저장.

String str = ""; // \n 전까지 c 값을 저장.

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
#define MPU6050_ACCEL_XOUT_H 0x3B // R
#define MPU6050_PWR_MGMT_1 0x6B // R/W
#define MPU6050_PWR_MGMT_2 0x6C // R/W
#define MPU6050_WHO_AM_I 0x75 // R
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

void draw(void) {
  // graphic commands to redraw the complete screen should be placed here 
 // u8g.drawBitmapP( 0, 5, 16, 50, logo_128x50);    // 라이브러리에서 세팅한 로고 출력을위한 드로우입니다.
  u8g.setFont(u8g_font_04b_03b);
  u8g.setPrintPos(5, 60);
  u8g.print("text_txt_out");  // 나중에 한글출력할수있게되면 로고밑에 한글 출력하고자 만들어둔.. ㅠㅠ
}

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
 Wire.begin();
 BTSerial.begin(9600);  // set the data rate for the BT port

 // default at power-up:
 // Gyro at 250 degrees second
 // Acceleration at 2g
 // Clock source at internal 8MHz
 // The device is in sleep mode.
 //
 error = MPU6050_read (MPU6050_WHO_AM_I, &c, 1);
 Serial.print(F("WHO_AM_I : "));
 Serial.print(c,HEX);
 Serial.print(F(", error = "));
 Serial.println(error,DEC);

 // According to the datasheet, the 'sleep' bit
 // should read a '1'. But I read a '0'.
 // That bit has to be cleared, since the sensor
 // is in sleep mode at power-up. Even if the
 // bit reads '0'.
 error = MPU6050_read (MPU6050_PWR_MGMT_2, &c, 1);
 Serial.print(F("PWR_MGMT_2 : "));
 Serial.print(c,HEX);
 Serial.print(F(", error = "));
 Serial.println(error,DEC);

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
// put your main code here, to run repeatedly:

 

  if(gpsSerial.available()) // gps 센서 통신 가능?

    {

      c=gpsSerial.read(); // 센서의 값 읽기

      if(c == '\n'){ // \n 값인지 구분.

        // \n 일시. 지금까지 저장된 str 값이 targetStr과 맞는지 구분

        if(targetStr.equals(str.substring(1, 6))){

          // NMEA 의 GPGGA 값일시

          Serial.println(str);

          // , 를 토큰으로서 파싱.

          int first = str.indexOf(",");

          int two = str.indexOf(",", first+1);

          int three = str.indexOf(",", two+1);

          int four = str.indexOf(",", three+1);

          int five = str.indexOf(",", four+1);

          // Lat과 Long 위치에 있는 값들을 index로 추출

          String Lat = str.substring(two+1, three);

          String Long = str.substring(four+1, five);

          // Lat의 앞값과 뒷값을 구분

          String Lat1 = Lat.substring(0, 2);

          String Lat2 = Lat.substring(2);

          // Long의 앞값과 뒷값을 구분

          String Long1 = Long.substring(0, 3);

          String Long2 = Long.substring(3);

          // 좌표 계산.

          double LatF = Lat1.toDouble() + Lat2.toDouble()/60;

          float LongF = Long1.toFloat() + Long2.toFloat()/60;

          // 좌표 출력.

          Serial.print("Lat : ");

          Serial.println(LatF, 15);

          Serial.print("Long : ");

          Serial.println(LongF, 15);

        

        }

        // str 값 초기화 

        str = "";

      }else{ // \n 아닐시, str에 문자를 계속 더하기

        str += c;

      }

    }
  /* MPU6050 FOR  STEP COUNT START */
   curSensoredTime = millis();
  u8g.firstPage(); 
  do {
   //if문을 넣어 루푸가 돌아가면서 일정수치 이하일때 특정 화면을 보여주고자 한건데

   //더좋은 방법이있다면 그걸 사용하세요..

   // 로고 -> 시계로고 -> 가속도 센서 수치값표기 



   if(draw_state < 130){
      draw_state++;
      draw();
    }else
    if( draw_state < 250){
      draw_state++;
     // u8g.drawBitmapP( 0, 5, 6, 48, TIME_logo_48x48);  //시계 아이콘 출력
      u8g.setFont(u8g_font_gdb11);
      u8g.setPrintPos(50, 38);
      u8g.print("C-Band");  // 이름출력
      u8g.setFont(u8g_font_5x8);
      u8g.setPrintPos(40, 52);
      u8g.print("Data loading...");  //그냥 뽀다구나게 데이타 불러오는 것처럼  ㅠ_ㅠ
    
    }else{
      u8g.setFont(u8g_font_04b_03b);
      u8g.setPrintPos(60, 10);
      u8g.print("ACCEL XYZ");
      u8g.setPrintPos(60, 19);
      u8g.print("X : ");u8g.print(X);
      u8g.setPrintPos(60, 26);
      u8g.print("Y : ");u8g.print(Y);
      u8g.setPrintPos(60, 33);
      u8g.print("Z : ");u8g.print(Z);
     
      u8g.setPrintPos(60, 45);
      u8g.print("Walks :");          // 값을 계산하여 걸음수출력 하고싶음
      u8g.setPrintPos(60, 52);
      u8g.print("Calorie(cal) :");  // 값을 계산하여 칼로리 수 출력하고싶음



         // 이곳에 결과값을 출력하는 명령을 넣으면됩니다.

         // 블루투스를 통해 전달받는 값을 출력해도 되겠죠.. ^^
      
    }
  } while( u8g.nextPage() );
 
 
  // Read from sensor
  if(curSensoredTime - prevSensoredTime > SENSOR_READ_INTERVAL) {
    readFromSensor();  // Read from sensor
    prevSensoredTime = curSensoredTime;
   
    // Send buffer data to remote
    if(iAccelIndex >= ACCEL_BUFFER_COUNT - 3) {
      sendToRemote();
      initBuffer();
      Serial.println("------------- Send 20 accel data to remote");
    }
  }
 /* MPU6050 FOR  STEP COUNT END */


  
  /*----------------------BPM SENSOR ---------------------------------------------------*/
    int iMy_BPM = pulseSensor.getBeatsPerMinute();  // Calls function on our pulseSensor object that returns BPM as an "int".
                                                    // "myBPM" hold this BPM value now.                                    
    itoa(iMy_BPM, chConvert_Int, 10);               // Convert integer to string 
                                                    // 매개변수 (바꿀 정수값, 바뀐 문자열 저장 공간, 진법)


    if (pulseSensor.sawStartOfBeat()) 
    {            
       bHeart_Status = !bHeart_Status;  // 반전
    }
    
    delay(20);                                      
    u8g.firstPage();  
    do 
    {
        Draw();
    } while( u8g.nextPage() );
/*----------------------BPM SENSOR END---------------------------------------------------*/



/* EMERGENCY BUTTON START */
    reading = digitalRead(sw);  // SW 상태 읽음
 
  //SW 가 눌려졌고 스위치 토글 눌림 경과시간이 Debounce 시간보다 크면 실행
  if (reading == HIGH && previous == LOW && millis() - time > debounce) {
    if (state == HIGH)    // LED 가 HIGH 면 LOW 로 바꿔준다.
      state = LOW;
    else                  // LED 가 LOW 면 HIGH 로 바꿔준다.
      state = HIGH;
 
    time = millis();
  }
 
  digitalWrite(led, state);
 
  previous = reading;

  /* EMERGENCY BUTTON END */
}
//-------------------BLUETOOTH------------------------------
/**************************************************
 * BT Transaction
 **************************************************/
void sendToRemote() {
  // Write gabage bytes
  BTSerial.write( "accel" );
  // Write accel data
  BTSerial.write( (char*)aAccelBuffer );
  // Flush buffer
  //BTSerial.flush();
}
//---------------------MPU 6050 SENSOR DATA FOR STEPCOUNT -----------------------
/**************************************************
 * Read data from sensor and save it
 **************************************************/
void readFromSensor() {
  int error;
  double dT;
  accel_t_gyro_union accel_t_gyro;
 
  error = MPU6050_read (MPU6050_ACCEL_XOUT_H, (uint8_t *) &accel_t_gyro, sizeof(accel_t_gyro));
  if(error != 0) {
    Serial.print(F("Read accel, temp and gyro, error = "));
    Serial.println(error,DEC);
  }
 
  // Swap all high and low bytes.
  // After this, the registers values are swapped,
  // so the structure name like x_accel_l does no
  // longer contain the lower byte.
  uint8_t swap;
  #define SWAP(x,y) swap = x; x = y; y = swap
  SWAP (accel_t_gyro.reg.x_accel_h, accel_t_gyro.reg.x_accel_l);
  SWAP (accel_t_gyro.reg.y_accel_h, accel_t_gyro.reg.y_accel_l);
  SWAP (accel_t_gyro.reg.z_accel_h, accel_t_gyro.reg.z_accel_l);
  SWAP (accel_t_gyro.reg.t_h, accel_t_gyro.reg.t_l);
  SWAP (accel_t_gyro.reg.x_gyro_h, accel_t_gyro.reg.x_gyro_l);
  SWAP (accel_t_gyro.reg.y_gyro_h, accel_t_gyro.reg.y_gyro_l);
  SWAP (accel_t_gyro.reg.z_gyro_h, accel_t_gyro.reg.z_gyro_l);
 
  // Print the raw acceleration values
  Serial.print(F("accel x,y,z: "));
  Serial.print(accel_t_gyro.value.x_accel, DEC);
  X = String(accel_t_gyro.value.x_accel);

  Serial.print(F(", "));
  Serial.print(accel_t_gyro.value.y_accel, DEC);
  Y = String(accel_t_gyro.value.y_accel);
 
  Serial.print(F(", "));
  Serial.print(accel_t_gyro.value.z_accel, DEC);
  Z = String(accel_t_gyro.value.z_accel);
 
  Serial.print(F(", at "));
  Serial.print(iAccelIndex);
  Serial.println(F(""));
 
  if(iAccelIndex < ACCEL_BUFFER_COUNT && iAccelIndex > 1) {
    int tempX = accel_t_gyro.value.x_accel;
    int tempY = accel_t_gyro.value.y_accel;
    int tempZ = accel_t_gyro.value.z_accel;
    /*
    // Check min, max value
    if(tempX > 16380) tempX = 16380;
    if(tempY > 16380) tempY = 16380;
    if(tempZ > 16380) tempZ = 16380;
   
    if(tempX < -16380) tempX = -16380;
    if(tempY < -16380) tempY = -16380;
    if(tempZ < -16380) tempZ = -16380;
   
    // We dont use negative value
    tempX += 16380;
    tempY += 16380;
    tempZ += 16380;
    */
    char temp = (char)(tempX >> 8);
    if(temp == 0x00)
      temp = 0x7f;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
    temp = (char)(tempX);
    if(temp == 0x00)
      temp = 0x01;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
   
    temp = (char)(tempY >> 8);
    if(temp == 0x00)
      temp = 0x7f;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
    temp = (char)(tempY);
    if(temp == 0x00)
      temp = 0x01;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
   
    temp = (char)(tempZ >> 8);
    if(temp == 0x00)
      temp = 0x7f;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
    temp = (char)(tempZ);
    if(temp == 0x00)
      temp = 0x01;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
  }
 
  // The temperature sensor is -40 to +85 degrees Celsius.
  // It is a signed integer.
  // According to the datasheet:
  // 340 per degrees Celsius, -512 at 35 degrees.
  // At 0 degrees: -512 - (340 * 35) = -12412
  //Serial.print(F("temperature: "));
  //dT = ( (double) accel_t_gyro.value.temperature + 12412.0) / 340.0;
  //Serial.print(dT, 3);
  //Serial.print(F(" degrees Celsius"));
  //Serial.println(F(""));

  // Print the raw gyro values.
//  Serial.print(F("gyro x,y,z : "));
//  Serial.print(accel_t_gyro.value.x_gyro, DEC);
//  Serial.print(F(", "));
//  Serial.print(accel_t_gyro.value.y_gyro, DEC);
//  Serial.print(F(", "));
//  Serial.print(accel_t_gyro.value.z_gyro, DEC);
//  Serial.println(F(""));
}

/**************************************************
 * MPU-6050 Sensor read/write
 **************************************************/
int MPU6050_read(int start, uint8_t *buffer, int size)
{
 int i, n, error;
 
 Wire.beginTransmission(MPU6050_I2C_ADDRESS);
 
 n = Wire.write(start);
 if (n != 1)
  return (-10);
 
 n = Wire.endTransmission(false); // hold the I2C-bus
 if (n != 0)
  return (n);
 
 // Third parameter is true: relase I2C-bus after data is read.
 Wire.requestFrom(MPU6050_I2C_ADDRESS, size, true);
 i = 0;
 while(Wire.available() && i<size)
 {
  buffer[i++]=Wire.read();
 }
 if ( i != size)
  return (-11);
 return (0); // return : no error
}

int MPU6050_write(int start, const uint8_t *pData, int size)
{
 int n, error;
 
 Wire.beginTransmission(MPU6050_I2C_ADDRESS);
 
 n = Wire.write(start); // write the start address
 if (n != 1)
  return (-20);
  
 n = Wire.write(pData, size); // write data bytes
 if (n != size)
  return (-21);
  
 error = Wire.endTransmission(true); // release the I2C-bus
 if (error != 0)
  return (error);
 return (0); // return : no error
}

int MPU6050_write_reg(int reg, uint8_t data)
{
 int error;
 error = MPU6050_write(reg, &data, 1);
 return (error);
}


/**************************************************
 * Utilities
 **************************************************/
void initBuffer() {
  iAccelIndex = 2;
  for(int i=iAccelIndex; i<ACCEL_BUFFER_COUNT; i++) {
    aAccelBuffer[i] = 0x00;
  }
  aAccelBuffer[0] = 0xfe;
  aAccelBuffer[1] = 0xfd;
  aAccelBuffer[122] = 0xfd;
  aAccelBuffer[123] = 0xfe;
  aAccelBuffer[124] = 0x00;
}
//-------------------BPM IMAGE DRAW-----------------------------------
const char rook_bitmap_Heart_1[] PROGMEM = {          // 빈하트
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 0
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 1
    0x06,0x30,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 2
    0x09,0x48,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 3
    0x10,0x84,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 4
    0x10,0x04,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 5
    0x10,0x04,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 6
    0x10,0x04,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 7
    0x08,0x08,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 8
    0x08,0x08,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 9
    0x04,0x10,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 10
    0x02,0x20,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 11
    0x01,0x40,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 12
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 13
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 14
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 15
};

const char rook_bitmap_Heart_2[] PROGMEM = {          // 풀 하트
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 0
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 1
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 2
    0x0E,0xE0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 3
    0x1F,0xF0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 4
    0x1F,0xF0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 5
    0x1F,0xF0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 6
    0x0F,0xE0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 7
    0x0F,0xE0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 8
    0x07,0xC0,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 9
    0x03,0x80,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 10
    0x01,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 11
    0x01,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 12
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 13
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 14
    0x00,0x00,  0x00,0x00,  0x00,0x00,  0x00,0x00,    // 15
};

void Draw(void) 
{
 
    // graphic commands to redraw the complete screen should be placed here  
    u8g.setFont(u8g_font_unifont);
    u8g.drawStr( 20, 30, "BPM");   // (행 ,열,문구)
    if(bHeart_Status)   u8g.drawBitmapP( 9, 40, 8, 16, rook_bitmap_Heart_1);    //(low,colunm,배열1,배열2, 함수명)
    else                u8g.drawBitmapP( 10, 40, 8, 16, rook_bitmap_Heart_2);  
    
    u8g.drawStr( 29, 52, chConvert_Int);                                       // (행 ,열,문구)
}
