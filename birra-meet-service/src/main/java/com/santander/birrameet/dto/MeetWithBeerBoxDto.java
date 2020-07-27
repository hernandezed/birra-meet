package com.santander.birrameet.dto;

import com.santander.birrameet.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeetWithBeerBoxDto {

    private String id;
    private String title;
    private String creator;
    private Integer participants;
    private LocalDateTime date;
    private Location location;
    private Long boxes;
    private Double temperature;
}
