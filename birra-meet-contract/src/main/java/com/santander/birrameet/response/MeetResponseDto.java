package com.santander.birrameet.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MeetResponseDto {

    private String id;
    private String title;
    private Integer participants;
    private LocalDateTime date;
    private LocationResponseDto location;
    private Long boxes;
    private Double temperature;

    @JsonCreator
    public MeetResponseDto(@JsonProperty String id, @JsonProperty String title, @JsonProperty Integer participants, @JsonProperty LocalDateTime date,
                           @JsonProperty LocationResponseDto location, @JsonProperty Long boxes, @JsonProperty Double temperature) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.date = date;
        this.location = location;
        this.boxes = boxes;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getParticipants() {
        return participants;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public LocationResponseDto getLocation() {
        return location;
    }

    public Long getBoxes() {
        return boxes;
    }

    public Double getTemperature() {
        return temperature;
    }
}
