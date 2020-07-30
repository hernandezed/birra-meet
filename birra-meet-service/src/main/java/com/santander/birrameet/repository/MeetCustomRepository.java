package com.santander.birrameet.repository;

import com.santander.birrameet.domain.Meet;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface MeetCustomRepository {
    Flux<Meet> findByDateGreaterThanEqualAndAndDateLessThan(LocalDateTime from, LocalDateTime to);
}
