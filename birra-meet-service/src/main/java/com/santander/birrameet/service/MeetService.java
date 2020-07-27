package com.santander.birrameet.service;


import com.santander.birrameet.dto.MeetWithBeerBoxDto;
import reactor.core.publisher.Mono;

public interface MeetService {

    Mono<MeetWithBeerBoxDto> findById(String id);
}
