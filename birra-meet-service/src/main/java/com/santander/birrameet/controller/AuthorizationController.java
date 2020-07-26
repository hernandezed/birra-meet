package com.santander.birrameet.controller;

import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getUsername(), request.getPassword())
                .map((token) -> ResponseEntity.ok(new LoginResponseDto(token)));
    }
}
