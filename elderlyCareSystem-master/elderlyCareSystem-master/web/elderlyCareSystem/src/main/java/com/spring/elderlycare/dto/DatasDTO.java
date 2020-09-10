package com.spring.elderlycare.dto;

import org.springframework.stereotype.Component;

@Component("DatasDTO")
public class DatasDTO {
	private int elderly;
	private float humid;
	private float temp;
	private boolean gas;
	private String measuredtime;
	
	public int getElderly() {
		return elderly;
	}
	public void setElderly(int elderly) {
		this.elderly = elderly;
	}
	public float getHumid() {
		return humid;
	}
	public void setHumid(float humid) {
		this.humid = humid;
	}
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	public boolean isGas() {
		return gas;
	}
	public void setGas(boolean gas) {
		this.gas = gas;
	}
	public String getMeasuredtime() {
		return measuredtime;
	}
	public void setMeasuredtime(String measuredtime) {
		this.measuredtime = measuredtime;
	}
	
	
}
