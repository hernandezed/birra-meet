package com.santander.birrameet.connectors.model.openWeather;

import java.util.List;

public class WeatherList {

    private long dt;

    private int sunrise;

    private int sunset;

    private Temp temp;

    private Feels_like feels_like;

    private int pressure;

    private int humidity;

    private List<Weather> weather;

    private double speed;

    private int deg;

    private int clouds;

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getDt() {
        return this.dt;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunrise() {
        return this.sunrise;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getSunset() {
        return this.sunset;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Temp getTemp() {
        return this.temp;
    }

    public void setFeels_like(Feels_like feels_like) {
        this.feels_like = feels_like;
    }

    public Feels_like getFeels_like() {
        return this.feels_like;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getPressure() {
        return this.pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public List<Weather> getWeather() {
        return this.weather;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getDeg() {
        return this.deg;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getClouds() {
        return this.clouds;
    }
}
