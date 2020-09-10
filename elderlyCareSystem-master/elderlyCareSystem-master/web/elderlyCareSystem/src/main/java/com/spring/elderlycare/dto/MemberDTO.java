package com.spring.elderlycare.dto;

import org.springframework.stereotype.Component;

@Component("MemberDTO")
public class MemberDTO {
	private String uid;
	private String upwd;
	private String uname;
	private String utel;
	private String uemail;
	private int urole;
	public MemberDTO() {
		
	}
	public MemberDTO(String uid, String upwd, String uname, String utel, String uemail) {
		this.uid = uid;
		this.upwd = upwd;
		this.uname = uname;
		this.utel = utel;
		this.uemail = uemail;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUtel() {
		return utel;
	}
	public void setUtel(String utel) {
		this.utel = utel;
	}
	public String getUemail() {
		return uemail;
	}
	public void setUemail(String uemail) {
		this.uemail = uemail;
	}
	public int getUrole() {
		return urole;
	}
	public void setUrole(int urole) {
		this.urole = urole;
	}
}
