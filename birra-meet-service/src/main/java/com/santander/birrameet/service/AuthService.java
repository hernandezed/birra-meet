package com.santander.birrameet.service;

import com.santander.birrameet.security.model.User;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<String> login(String username, String password);

    Mono<Void> signUp(String username, String password);
}
