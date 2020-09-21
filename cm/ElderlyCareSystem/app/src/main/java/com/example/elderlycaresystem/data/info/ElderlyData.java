package com.example.elderlycaresystem.data.info;

import android.os.health.TimerStat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class ElderlyData {
    private String measuredtime;
    private int ekey;
    private int estep;
    private int epulse;
    private double ekcal;
    private double ealtitude;
    private double elongitude;
    private int stat;
    private float humid;
    private float temp;

    public String getMeasuredtime() { return measuredtime; }

    public void setMeasuredtime(String measuredtime) {
        this.measuredtime = measuredtime;
    }

    public int getEkey() {
        return ekey;
    }

    public void setEkey(int ekey) {
        this.ekey = ekey;
    }

    public int getEstep() {
        return estep;
    }

    public void setEstep(int estep) {
        this.estep = estep;
    }

    public int getEpulse() {
        return epulse;
    }

    public void setEpulse(int epulse) {
        this.epulse = epulse;
    }

    public double getEkcal() {
        return ekcal;
    }

    public void setEkcal(double ekcal) {
        this.ekcal = ekcal;
    }

    public double getEaltitude() {
        return ealtitude;
    }

    public void setEaltitude(double ealtitude) {
        this.ealtitude = ealtitude;
    }

    public double getElongitude() {
        return elongitude;
    }

    public void setElongitude(double elongitude) {
        this.elongitude = elongitude;
    }

    public int getStat() { return stat; }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public float getHumid() { return humid; }

    public void setHumid(float humid) { this.humid = humid; }

    public float getTemp() { return temp; }

    public void setTemp(float temp) { this.temp = temp; }
}
