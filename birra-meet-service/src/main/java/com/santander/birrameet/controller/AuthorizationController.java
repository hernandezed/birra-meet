package com.santander.birrameet.controller;

import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getUsername(), request.getPassword())
                .map(LoginResponseDto::new);
    }
}
