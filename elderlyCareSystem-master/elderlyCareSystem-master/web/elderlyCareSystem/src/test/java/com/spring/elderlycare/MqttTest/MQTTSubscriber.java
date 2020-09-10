package com.spring.elderlycare.MqttTest;


import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/*
 * ref
 * https://www.monirthought.com/2017/11/eclipse-paho-java-client-mqtt-client.html
 * 
 */
@Component
public class MQTTSubscriber extends MQTTConfig implements MqttCallback{
	
	private String brokerURL = null;
	final private String colon = ":";
	final private String clientId = "user00(test)";
	
	private MqttClient mqttClient = null;
	private MqttConnectOptions connectOptions = null;
	private MemoryPersistence persistence = null;
	
	private static final Logger logger = LoggerFactory.getLogger(MQTTSubscriber.class);

	
	public MQTTSubscriber() {
		logger.info("init");
		//this.config();
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		logger.info("connection lost");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		String time = new Timestamp(System.currentTimeMillis()).toString();
		System.out.println();
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Message Arrived at Time: " + time + "  Topic: " + topic + "  Message: "
		  + new String(message.getPayload()));
		System.out.println("***********************************************************************");
		System.out.println();
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
	public void subscribeMessage(String topic) {
		 logger.info("subscribe Message");
	  try {
	   this.mqttClient.subscribe(topic, this.qos);
	  } catch (MqttException me) {
	   me.printStackTrace();
	  }
	 }
	
	 public void disconnect() {
		 logger.info("disconnect");
		  try {
		   this.mqttClient.disconnect();
		  } catch (MqttException me) {
		   logger.error("ERROR", me);
		  }
		 }

	@Override
	/*protected*/public void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {
		// TODO Auto-generated method stub
		String protocol = ssl?this.SSL:this.TCP;
		this.brokerURL = protocol+broker+colon+port;
		this.persistence = new MemoryPersistence();
		this.connectOptions = new MqttConnectOptions();
		
		logger.info(brokerURL);
		try {
			this.mqttClient = new MqttClient(brokerURL, clientId, persistence);
			this.connectOptions.setCleanSession(true);
			if(withUserNamePass) {
				if(password!=null) this.connectOptions.setPassword(this.password.toCharArray());
				if(userName !=null) this.connectOptions.setUserName(this.userName);
			}
			this.mqttClient.connect(this.connectOptions);
			this.mqttClient.setCallback(this);
		}catch(MqttException me) {
			me.printStackTrace();
		}
	}

	@Override
	/*protected*/public void config() {
		logger.info("config()");
		// TODO Auto-generated method stub
		this.brokerURL = this.TCP+this.broker+colon+this.port;
		this.persistence = new MemoryPersistence();
		this.connectOptions = new MqttConnectOptions();
		try {
			  this.mqttClient = new MqttClient(brokerURL, clientId, persistence);
			  this.connectOptions.setCleanSession(true);
			  this.mqttClient.connect(this.connectOptions);
			  this.mqttClient.setCallback(this);
			 } catch (MqttException me) {
			  me.printStackTrace();
		  }
	}

}
