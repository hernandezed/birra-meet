package com.santander.birrameet.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.santander.birrameet.commons.LocationApiDto;

import java.time.LocalDateTime;

public class MeetResponseDto {

    private String id;
    private String title;
    private Integer participants;
    private LocalDateTime date;
    private LocationApiDto location;
    private Long boxes;
    private Double temperature;
    private String creator;

    @JsonCreator
    public MeetResponseDto(@JsonProperty String id, @JsonProperty String title, @JsonProperty Integer participants, @JsonProperty LocalDateTime date,
                           @JsonProperty LocationApiDto location, @JsonProperty Long boxes, @JsonProperty Double temperature, @JsonProperty String creator) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.date = date;
        this.location = location;
        this.boxes = boxes;
        this.temperature = temperature;
        this.creator = creator;
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

    public LocationApiDto getLocation() {
        return location;
    }

    public Long getBoxes() {
        return boxes;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getCreator() {
        return creator;
    }
}
