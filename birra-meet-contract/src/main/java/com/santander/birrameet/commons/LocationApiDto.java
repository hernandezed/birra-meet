package com.santander.birrameet.commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationApiDto {
    private Double longitude;
    private Double latitude;

    @JsonCreator
    public LocationApiDto(@JsonProperty("longitude") Double longitude, @JsonProperty("latitude") Double latitude) {
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
