package com.santander.birrameet.service;

import com.santander.birrameet.domain.Meet;
import reactor.core.publisher.Mono;

public interface MeetService {

    Mono<Meet> findById(String id);
}
