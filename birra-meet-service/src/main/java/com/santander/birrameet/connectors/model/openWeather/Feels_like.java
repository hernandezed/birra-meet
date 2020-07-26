package com.santander.birrameet.connectors.model.openWeather;

public class Feels_like {
    private double day;

    private double night;

    private double eve;

    private double morn;

    public void setDay(double day) {
        this.day = day;
    }

    public double getDay() {
        return this.day;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getNight() {
        return this.night;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getEve() {
        return this.eve;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public double getMorn() {
        return this.morn;
    }
}