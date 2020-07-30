package com.santander.birrameet.repository.impl;

import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.repository.MeetCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class MeetCustomRepositoryImpl implements MeetCustomRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Meet> findByDateGreaterThanEqualAndAndDateLessThan(LocalDateTime from, LocalDateTime to) {
        return mongoTemplate.find(Query.query(Criteria.where("").andOperator(Criteria.where("date").gte(from), Criteria.where("date").lt(to))), Meet.class);
    }
}
