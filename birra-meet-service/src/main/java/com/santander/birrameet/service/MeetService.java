package com.santander.birrameet.service;


import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.dto.MeetDto;
import reactor.core.publisher.Mono;

public interface MeetService {

    Mono<MeetDto> findById(String id);

    Mono<MeetDto> create(Meet meet);

    Mono<MeetDto> enroll(String id);

    Mono<MeetDto> checkin(String id);
}
