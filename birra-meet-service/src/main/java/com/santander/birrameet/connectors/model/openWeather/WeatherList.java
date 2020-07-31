package com.santander.birrameet.connectors.model.openWeather;

public class WeatherList {

    private long dt;
    private Temp temp;

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getDt() {
        return this.dt;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Temp getTemp() {
        return this.temp;
    }
}
