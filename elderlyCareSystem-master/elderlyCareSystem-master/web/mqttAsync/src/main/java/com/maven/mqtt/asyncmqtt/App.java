package com.maven.mqtt.asyncmqtt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Hello world!
 *
 */
public class App 
{
	private final static Logger LOG = Logger.getGlobal();
    public static void main( String[] args ) throws SecurityException, IOException 
    {
    	
    	Logger rootLogger = Logger.getLogger("");
    	Handler[] handlers = rootLogger.getHandlers();
    	if(handlers[0] instanceof ConsoleHandler) {
    		rootLogger.removeHandler(handlers[0]);
    	}
    	LOG.setLevel(Level.INFO);
        
        Handler handler = new FileHandler("D:\\1elderlyproject\\web\\mqttlog\\mylog.log", true);
        LOG.addHandler(handler);
        
    	MqttService client = new MqttService(LOG);
		/*try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = 
					new SqlSessionFactoryBuilder().build(inputStream);
			
			sqlSession = sqlSessionFactory.openSession(true);
		}catch(IOException e) {
			e.printStackTrace();
			return;
		}*/
		List<Map<String, Object>> list = client.getIoTList();
		LOG.info("test");
		for(Map<String,Object> m : list) {
			//System.out.println(m.get("homeIoT"));
			client.mqttSubscribe((String)m.get("homeIoT"), 1883, "home/#");
			//client.mqttSubscribe("127.0.0.1", 1883, "home/#");
			//client.mqttSubscribe("121.138.83.121", 1883, "home/#");
		}
    }
}
