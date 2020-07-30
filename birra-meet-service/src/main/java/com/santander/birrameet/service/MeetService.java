package com.santander.birrameet.service;


import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.dto.MeetDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface MeetService {

    Mono<MeetDto> findById(String id);

    Mono<MeetDto> create(Meet meet);

    Mono<MeetDto> enroll(String id);

    Mono<MeetDto> checkin(String id);

    Flux<MeetDto> updateTemperature();
}
