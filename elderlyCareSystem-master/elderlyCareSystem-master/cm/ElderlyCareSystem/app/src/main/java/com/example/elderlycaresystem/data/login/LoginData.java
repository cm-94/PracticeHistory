package com.example.elderlycaresystem.data.login;

public class LoginData {
    String uid;
    String upwd;
    String regId;

    public LoginData(){

    }

    public LoginData(String id, String pwd,String regId){
        this.uid = id;
        this.upwd = pwd;
        this.regId = regId;
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

    public String getRegId() { return regId; }

    public void setRegId(String regId) { this.regId = regId; }
}
