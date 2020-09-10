package com.spring.elderlycare.MqttTest;

import org.springframework.stereotype.Component;

/*https://www.monirthought.com/2017/11/eclipse-paho-java-client-mqtt-client.html*/
@Component
public abstract class MQTTConfig {

	 protected final String broker = "iot.eclipse.org";
	 protected final int qos = 2;
	 protected Boolean hasSSL = false; /* By default SSL is disabled */
	 protected Integer port = 1883; /* Default port */
	 protected final String userName = "testUserName";
	 protected final String password = "demoPassword";
	 protected final String TCP = "tcp://";
	 protected final String SSL = "ssl://";

	 /**
	  * Custom Configuration
	  * 
	  * @param broker
	  * @param port
	  * @param ssl
	  * @param withUserNamePass
	  */
	 protected abstract void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass);

	 /**
	  * Default Configuration
	  */
	 protected abstract void config();
	}
