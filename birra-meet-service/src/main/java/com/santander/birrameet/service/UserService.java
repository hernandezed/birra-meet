package com.santander.birrameet.service;

import com.santander.birrameet.security.model.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> findByUsername(String username);
}
