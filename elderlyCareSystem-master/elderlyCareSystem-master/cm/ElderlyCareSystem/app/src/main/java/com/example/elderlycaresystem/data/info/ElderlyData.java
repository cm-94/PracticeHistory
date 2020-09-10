package com.example.elderlycaresystem.data.info;

import android.os.health.TimerStat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class ElderlyData {
    private String measuredtime;
    private Integer ekey;
    private Integer estep;
    private Integer epulse;
    private Double ekcal;
    private Double ealtitude;
    private Double elongitude;
    private Integer stat;
    private float humid;
    private float temp;

    public String getMeasuredtime() { return measuredtime; }

    public void setMeasuredtime(String measuredtime) {
        this.measuredtime = measuredtime;
    }

    public Integer getEkey() {
        return ekey;
    }

    public void setEkey(Integer ekey) {
        this.ekey = ekey;
    }

    public Integer getEstep() {
        return estep;
    }

    public void setEstep(Integer estep) {
        this.estep = estep;
    }

    public Integer getEpulse() {
        return epulse;
    }

    public void setEpulse(Integer epulse) {
        this.epulse = epulse;
    }

    public Double getEkcal() {
        return ekcal;
    }

    public void setEkcal(Double ekcal) {
        this.ekcal = ekcal;
    }

    public Double getEaltitude() {
        return ealtitude;
    }

    public void setEaltitude(Double ealtitude) {
        this.ealtitude = ealtitude;
    }

    public Double getElongitude() {
        return elongitude;
    }

    public void setElongitude(Double elongitude) {
        this.elongitude = elongitude;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public float getHumid() { return humid; }

    public void setHumid(float humid) { this.humid = humid; }

    public float getTemp() { return temp; }

    public void setTemp(float temp) { this.temp = temp; }
}
