package com.santander.birrameet.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationResponseDto {
    private Double longitude;
    private Double latitude;

    @JsonCreator
    public LocationResponseDto(@JsonProperty Double longitude, @JsonProperty Double latitude) {
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
