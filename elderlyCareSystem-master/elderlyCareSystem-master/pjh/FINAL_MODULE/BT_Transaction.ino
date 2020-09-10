/**************************************************
   BT Transaction
 **************************************************/
void sendToRemote() {
  // Write gabage bytes
  BTSerial.write( "accel" );
  // Write accel data
  BTSerial.write( (char*)aAccelBuffer );
  // Flush buffer
  //BTSerial.flush();
}
