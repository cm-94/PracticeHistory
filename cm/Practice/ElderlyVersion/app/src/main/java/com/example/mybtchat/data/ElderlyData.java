package com.example.mybtchat.data;

public class ElderlyData {
    private String measuredtime;
    private int ekey;
    private int estep;
    private int epulse;
    private double ekcal;
    private double ealtitude;
    private double elongitude;
    private int stat;


    public ElderlyData(){
    }

    public ElderlyData(int key, int step, int pulse,int state, double kcal, double latitude, double longitude){
        ekey = key;
        estep = step;
        epulse = pulse;
        stat = state;
        ekcal = kcal;
        ealtitude = latitude;
        elongitude = longitude;
    }

    public int getEkey() { return ekey; }

    public void setEkey(int ekey) { this.ekey = ekey; }

    public int getEstep() { return estep; }

    public void setEstep(int estep) { this.estep = estep; }

    public int getEpulse() { return epulse; }

    public void setEpulse(int epulse) { this.epulse = epulse;
    }

    public int getEstat() { return stat; }

    public void setEstat(int estat) { this.stat = estat; }

    public double getEkcal() { return ekcal; }

    public void setEkcal(double ekcal) { this.ekcal = ekcal; }

    public double getElatitude() { return ealtitude; }

    public void setElatitude(double elatitude) { this.ealtitude = elatitude; }

    public double getElongitude() { return elongitude; }

    public void setElongitude(double elongitude) { this.elongitude = elongitude; }
}