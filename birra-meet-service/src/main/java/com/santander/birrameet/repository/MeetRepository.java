package com.santander.birrameet.repository;

import com.santander.birrameet.domain.Meet;
import org.bson.types.ObjectId;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

public interface MeetRepository extends Repository<Meet, ObjectId> {

    Mono<Meet> findById(ObjectId id);

}
