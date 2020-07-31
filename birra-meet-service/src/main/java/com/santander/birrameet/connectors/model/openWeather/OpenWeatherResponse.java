package com.santander.birrameet.connectors.model.openWeather;

import java.util.List;

public class OpenWeatherResponse {

    private List<WeatherList> list;

    public void setList(List<WeatherList> list) {
        this.list = list;
    }

    public List<WeatherList> getList() {
        return this.list;
    }
}

