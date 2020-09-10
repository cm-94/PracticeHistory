package com.spring.elderlycare.dto;

import org.springframework.stereotype.Component;

@Component("DevicesDTO")
public class DevicesDTO {
	private int elderly;
	private String homeIoT;
	private String bandIoT;
	
	public int getElderly() {
		return elderly;
	}
	public void setElderly(int elderly) {
		this.elderly = elderly;
	}
	public String getHomeIoT() {
		return homeIoT;
	}
	public void setHomeIoT(String homeIoT) {
		this.homeIoT = homeIoT;
	}
	public String getBandIoT() {
		return bandIoT;
	}
	public void setBandIoT(String bandIoT) {
		this.bandIoT = bandIoT;
	}
	
}
