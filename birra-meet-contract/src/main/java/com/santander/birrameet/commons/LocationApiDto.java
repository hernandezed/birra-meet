package com.santander.birrameet.commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationApiDto {
    private Double longitude;
    private Double latitude;

    @JsonCreator
    public LocationApiDto(@JsonProperty("longitude") double longitude, @JsonProperty("latitude") double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
