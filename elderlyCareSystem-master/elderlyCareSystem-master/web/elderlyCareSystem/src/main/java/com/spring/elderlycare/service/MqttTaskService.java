package com.spring.elderlycare.service;

import java.util.List;
import java.util.Map;

//@Component
public interface MqttTaskService {
	public void mqttSubscribe(String broker, int port, String topic);
	public List<Map<String, Object>> getIPList();
	
}
