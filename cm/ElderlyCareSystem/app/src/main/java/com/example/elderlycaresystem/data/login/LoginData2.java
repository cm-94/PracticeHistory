package com.example.elderlycaresystem.data.login;

public class LoginData2 {
    private String uid;
    private String upwd;
    private String regid;

    public LoginData2(String id, String pwd,String reg){
        this.uid = id;
        this.upwd = pwd;
        this.regid = reg;
        this.regid = "cU5SXNtAK1M:APA91bGCXzrFsivGR2bN1icHZM1xbXqr22UYEvOh5Tj6Sysk5RjuE7daZqkoluOhwUwqCSghaBl2iWDmEgCvjCI-7SNgwo_6FSYwUL2VsuYhF5ABQdWC818DU5UPvNkBxQR4-6vONVl5";
    }

    public String getRegid() { return regid; }

    public void setRegid(String regid) { this.regid = regid; }

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
}
