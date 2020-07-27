package com.santander.birrameet.connectors.model.openWeather;

public class Coord {
    private int lon;
    private int lat;

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLon() {
        return this.lon;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLat() {
        return this.lat;
    }
}
