/**************************************************
   Utilities
 **************************************************/
void initBuffer() {
  iAccelIndex = 2;
  for (int i = iAccelIndex; i < ACCEL_BUFFER_COUNT; i++) {
    aAccelBuffer[i] = 0x00;
  }
  aAccelBuffer[0] = 0xfe;
  aAccelBuffer[1] = 0xfd;
  aAccelBuffer[122] = 0xfd;
  aAccelBuffer[123] = 0xfe;
  aAccelBuffer[124] = 0x00;
}
