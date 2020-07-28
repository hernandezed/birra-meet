package com.santander.birrameet.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.santander.birrameet.commons.LocationApiDto;

import java.time.LocalDateTime;

public class MeetCreateRequestDto {
    private String title;
    private LocalDateTime date;
    private LocationApiDto location;

    @JsonCreator
    public MeetCreateRequestDto(@JsonProperty("title") String title, @JsonProperty("date") LocalDateTime date, @JsonProperty("location") LocationApiDto location) {
        this.title = title;
        this.date = date;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public LocationApiDto getLocation() {
        return location;
    }
}
