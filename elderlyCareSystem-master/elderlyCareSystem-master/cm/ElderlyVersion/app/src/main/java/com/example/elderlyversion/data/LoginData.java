package com.example.elderlyversion.data;

public class LoginData {
    private String ename;
    private String ebirth;

    public LoginData(String name, String birth){
        ename = name;
        ebirth = birth;
    }

    public String getuName() { return ename; }

    public void setuName(String uName) { this.ename = uName; }

    public String getuBirth() { return ebirth; }

    public void setuBirth(String uBirth) { this.ebirth = uBirth; }
}
