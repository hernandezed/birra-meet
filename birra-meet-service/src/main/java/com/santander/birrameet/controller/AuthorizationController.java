package com.santander.birrameet.controller;

import com.santander.birrameet.exceptions.DuplicateUsernameException;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.request.SignUpRequestDto;
import com.santander.birrameet.response.ApiError;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/login")
    @Tag(name = "login", description = "Allows the user to authenticate to access the services that require it")
    public Mono<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getUsername(), request.getPassword())
                .map(LoginResponseDto::new);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Tag(name = "sign-up", description = "Allows the user to create credentials in app")
    public Mono<Void> signUp(@RequestBody SignUpRequestDto request) {
        return authService.signUp(request.getUsername(), request.getPassword());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiError> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        ApiError apiError = new ApiError(4041, "Username already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}
