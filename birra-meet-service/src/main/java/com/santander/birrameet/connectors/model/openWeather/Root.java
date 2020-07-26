package com.santander.birrameet.connectors.model.openWeather;

import java.util.List;

public class Root {
    private City city;

    private String code;

    private double message;

    private int cnt;

    private List<WeatherList> list;

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return this.city;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public double getMessage() {
        return this.message;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return this.cnt;
    }

    public void setList(List<WeatherList> list) {
        this.list = list;
    }

    public List<WeatherList> getList() {
        return this.list;
    }
}

