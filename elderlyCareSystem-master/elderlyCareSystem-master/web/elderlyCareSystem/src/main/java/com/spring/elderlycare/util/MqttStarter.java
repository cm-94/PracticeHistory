package com.spring.elderlycare.util;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class MqttStarter {
	// 리스트 불러와서 사용
	
	 private final String mqttProcess = "D:\\1elderlyproject\\web\\mqttasyncht-rmdata_log.jar"; //로컬호스트 하나 
	 
	 //(2)private final MqttBean bean = new MqttBean(); 
	//@Autowired MqttTaskService service;
	
	public void mqttstart() { 
		
	
		 try { 
			Runtime runtime = Runtime.getRuntime(); 
			runtime.exec("java -jar "+mqttProcess); 
	 
		 }catch(Exception e ) { 
			 e.printStackTrace(); 
		 }
		
	 }
	
	  public void mqttdestroy() { 
		  //future.cancel(true); 
		
		  	try {
				Runtime.getRuntime().exec("taskkill /F /IM java.exe"); 
			} catch (IOException e) {
				  // TODO Auto-generated catch block e.printStackTrace(); } }
			}
			  
	  }
	 
}
