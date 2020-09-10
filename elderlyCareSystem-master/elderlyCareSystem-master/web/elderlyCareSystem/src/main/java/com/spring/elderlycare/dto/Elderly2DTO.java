package com.spring.elderlycare.dto;

import org.springframework.stereotype.Component;

@Component("Elderly2DTO")
public class Elderly2DTO {
	private int ekey;
	private String ename;
	private String ebirth;
	private String etel;
	private String eaddr;
	private String homeIoT;
	private int stat;
	
	public int getEkey() {
		return ekey;
	}
	public void setEkey(int ekey) {
		this.ekey = ekey;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEbirth() {
		return ebirth;
	}
	public void setEbirth(String ebirth) {
		this.ebirth = ebirth;
	}
	public String getEtel() {
		return etel;
	}
	public void setEtel(String etel) {
		this.etel = etel;
	}
	public String getEaddr() {
		return eaddr;
	}
	public void setEaddr(String eaddr) {
		this.eaddr = eaddr;
	}
	public String getHomeIoT() {
		return homeIoT;
	}
	public void setHomeIoT(String homeIoT) {
		this.homeIoT = homeIoT;
	}
	public int getStat() {
		return stat;
	}
	public void setStat(int stat) {
		this.stat = stat;
	}
	
	
}
