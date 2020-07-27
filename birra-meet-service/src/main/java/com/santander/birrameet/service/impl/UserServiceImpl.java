package com.santander.birrameet.service.impl;

import com.santander.birrameet.repository.UserRepository;
import com.santander.birrameet.security.model.User;
import com.santander.birrameet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
