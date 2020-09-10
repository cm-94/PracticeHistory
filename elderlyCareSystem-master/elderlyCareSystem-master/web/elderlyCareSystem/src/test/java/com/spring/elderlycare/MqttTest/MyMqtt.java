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


/*
 * ref
 * https://www.monirthought.com/2017/11/eclipse-paho-java-client-mqtt-client.html
 * 
 */
public class MyMqtt extends MQTTConfig implements MqttCallback{
	
	private String brokerURL = null;
	final private String colon = ":";
	final private String clientId = "user00(test)";
	
	private MqttClient mqttClient = null;
	private MqttConnectOptions connectOptions = null;
	private MemoryPersistence persistence = null;
	
	private static final Logger logger = LoggerFactory.getLogger(MyMqtt.class);
	
	private MyMqtt() {
		  this.config();
		 }

		 /**
		  * Private constructor
		  */
		 private MyMqtt(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {
		  this.config(broker, port, ssl, withUserNamePass);
		 }
	 /**
	  * Factory method to get instance of MQTTPublisher
	  * 
	  * @return MQTTPublisher
	  */
	 public static MyMqtt getInstance() {
	  return new MyMqtt();
	 }

	 /**
	  * Factory method to get instance of MQTTPublisher
	  * 
	  * @param broker
	  * @param port
	  * @param ssl
	  * @param withUserNamePass
	  * @return MQTTPublisher
	  */
	 public static MyMqtt getInstance(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {
	  return new MyMqtt(broker, port, ssl, withUserNamePass);
	 }
	
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		logger.info("connection lost");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		logger.info("message arrived");
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		logger.info("delivery completed");
		
	}
	
	public void publishMessage(String topic, String message) {
		 logger.info("publish Message");
	  try {
		  MqttMessage mqttmessage = new MqttMessage(message.getBytes());
		  mqttmessage.setQos(this.qos);
	   this.mqttClient.publish(topic, mqttmessage);
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
	 protected void config() {

	  this.brokerURL = this.TCP + this.broker + colon + this.port;
	  this.persistence = new MemoryPersistence();
	  this.connectOptions = new MqttConnectOptions();
	  try {
	   this.mqttClient = new MqttClient(brokerURL, clientId, persistence);
	   this.connectOptions.setCleanSession(true);
	   this.mqttClient.connect(this.connectOptions);
	   this.mqttClient.setCallback(this);
	  } catch (MqttException me) {
	   logger.error("ERROR", me);
	  }
	 }

	 /*
	  * (non-Javadoc)
	  * 
	  * @see
	  * com.bjitgroup.jasmysp.mqtt.publisher.MQTTPublisherBase#configurePublisher(
	  * java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
	  */
	 @Override
	 protected void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {

	  String protocal = this.TCP;
	  if (true == ssl) {
	   protocal = this.SSL;
	  }

	  this.brokerURL = protocal + broker + colon + port;
	  this.persistence = new MemoryPersistence();
	  this.connectOptions = new MqttConnectOptions();

	  try {
	   this.mqttClient = new MqttClient(brokerURL, clientId, persistence);
	   this.connectOptions.setCleanSession(true);
	   if (true == withUserNamePass) {
	    if (password != null) {
	     this.connectOptions.setPassword(this.password.toCharArray());
	    }
	    if (userName != null) {
	     this.connectOptions.setUserName(this.userName);
	    }
	   }
	   this.mqttClient.connect(this.connectOptions);
	   this.mqttClient.setCallback(this);
	  } catch (MqttException me) {
	   logger.error("ERROR", me);
	  }
	 }

}
