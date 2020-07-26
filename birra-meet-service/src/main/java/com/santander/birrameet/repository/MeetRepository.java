package com.santander.birrameet.repository;

import com.santander.birrameet.domain.Meet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MeetRepository extends ReactiveMongoRepository<Meet, ObjectId> {

    Mono<Meet> findById(ObjectId id);

}
