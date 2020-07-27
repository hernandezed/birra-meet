package com.santander.birrameet.service;

import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<String> login(String username, String password);
}
