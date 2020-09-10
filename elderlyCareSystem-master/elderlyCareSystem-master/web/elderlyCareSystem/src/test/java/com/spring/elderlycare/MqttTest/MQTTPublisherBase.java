package com.spring.elderlycare.MqttTest;

/*https://www.monirthought.com/2017/11/eclipse-paho-java-client-mqtt-client.html*/
public interface MQTTPublisherBase {

	 /**
	  * Publish message
	  * 
	  * @param topic
	  * @param jasonMessage
	  */
	 public void publishMessage(String topic, String message);

	 /**
	  * Disconnect MQTT Client
	  */
	 public void disconnect();

}