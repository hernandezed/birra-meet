package com.santander.birrameet.repository;

import com.santander.birrameet.security.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {
    Mono<User> findByUsername(String username);
}
