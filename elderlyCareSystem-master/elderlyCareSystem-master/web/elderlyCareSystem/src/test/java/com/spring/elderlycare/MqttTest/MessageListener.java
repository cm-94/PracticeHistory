package com.spring.elderlycare.MqttTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*https://www.monirthought.com/2017/11/eclipse-paho-java-client-mqtt-client.html*/
@Component
public class MessageListener implements Runnable{

 @Autowired
 MQTTSubscriberBase subscriber;
 
 @Override
 public void run() {
  while(true) {
   subscriber.subscribeMessage("demoTopic2017");
  }
  
 }

}