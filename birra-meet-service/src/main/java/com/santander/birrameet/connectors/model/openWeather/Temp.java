package com.santander.birrameet.connectors.model.openWeather;

public class Temp {
    private double day;

    private double min;

    private double max;

    private double night;

    private double eve;

    private double morn;

    public void setDay(double day) {
        this.day = day;
    }

    public double getDay() {
        return this.day;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMin() {
        return this.min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMax() {
        return this.max;
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
