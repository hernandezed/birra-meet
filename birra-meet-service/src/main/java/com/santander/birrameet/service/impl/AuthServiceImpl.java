package com.santander.birrameet.service.impl;

import com.santander.birrameet.exceptions.InvalidUsernameOrPasswordException;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import com.santander.birrameet.security.utils.JWTUtils;
import com.santander.birrameet.service.AuthService;
import com.santander.birrameet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> login(String username, String password) {
        return userService.findByUsername(username).map(user -> {
            if (Objects.nonNull(user) && passwordEncoder.matches(password, user.getPassword())) {
                return jwtUtils.generateToken(user);
            } else {
                throw new InvalidUsernameOrPasswordException();
            }
        });
    }

    @Override
    public Mono<User> signUp(String username, String password) {
        User newUser = new User(null, username, passwordEncoder.encode(password), true, List.of(Role.ROLE_USER));
        return userService.insert(newUser);

    }
}
