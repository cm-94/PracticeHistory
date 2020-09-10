/**************************************************
   Read data from sensor and save it
 **************************************************/
void readFromSensor() {
  int error;
  double dT;
  accel_t_gyro_union accel_t_gyro;

  error = MPU6050_read (MPU6050_ACCEL_XOUT_H, (uint8_t *) &accel_t_gyro, sizeof(accel_t_gyro));
  if (error != 0) {
    Serial.print(F("Read accel, temp and gyro, error = "));
    Serial.println(error, DEC);
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

  if (iAccelIndex < ACCEL_BUFFER_COUNT && iAccelIndex > 1) {
    int tempX = accel_t_gyro.value.x_accel;
    int tempY = accel_t_gyro.value.y_accel;
    int tempZ = accel_t_gyro.value.z_accel;

    char temp = (char)(tempX >> 8);
    if (temp == 0x00)
      temp = 0x7f;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
    temp = (char)(tempX);
    if (temp == 0x00)
      temp = 0x01;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;

    temp = (char)(tempY >> 8);
    if (temp == 0x00)
      temp = 0x7f;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
    temp = (char)(tempY);
    if (temp == 0x00)
      temp = 0x01;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;

    temp = (char)(tempZ >> 8);
    if (temp == 0x00)
      temp = 0x7f;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
    temp = (char)(tempZ);
    if (temp == 0x00)
      temp = 0x01;
    aAccelBuffer[iAccelIndex] = temp;
    iAccelIndex++;
  }
}
