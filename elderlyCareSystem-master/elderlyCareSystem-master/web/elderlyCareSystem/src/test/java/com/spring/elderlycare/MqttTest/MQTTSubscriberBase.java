package com.spring.elderlycare.MqttTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*https://www.monirthought.com/2017/11/eclipse-paho-java-client-mqtt-client.html*/
public interface MQTTSubscriberBase {

	 public static final Logger logger = LoggerFactory.getLogger(MQTTPublisherBase.class);

	 /**
	  * Subscribe message
	  * 
	  * @param topic
	  * @param jasonMessage
	  */
	 public void subscribeMessage(String topic);

	 /**
	  * Disconnect MQTT Client
	  */
	 public void disconnect();
	}