package com.santander.birrameet.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.dto.MeetDto;
import com.santander.birrameet.exceptions.IntegrationError;
import com.santander.birrameet.exceptions.InvalidCheckinException;
import com.santander.birrameet.exceptions.InvalidEnrollException;
import com.santander.birrameet.request.MeetCreateRequestDto;
import com.santander.birrameet.response.ApiError;
import com.santander.birrameet.response.MeetResponseDto;
import com.santander.birrameet.service.MeetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("meet")
public class MeetController {

    private final MeetService meetService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    @Tag(name = "find", description = "Returns a meet given an id. If the user has the Admin role, it also returns the number of beer boxes to buy")
    public Mono<MeetResponseDto> find(@PathVariable String id) {
        return meetService.findById(id).map(meet -> objectMapper.convertValue(meet, MeetResponseDto.class));
    }

    @PostMapping("")
    @Tag(name = "create", description = "Create a new meet. Only users with role admin can use this endpoint.")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MeetResponseDto> create(@RequestBody MeetCreateRequestDto meetCreateRequestDto) {
        return meetService.create(objectMapper.convertValue(meetCreateRequestDto, Meet.class)).map(meet -> objectMapper.convertValue(meet, MeetResponseDto.class));
    }

    @PatchMapping("/{id}/enroll")
    @Tag(name = "enroll", description = "Enroll a user in an existing meet")
    public Mono<MeetResponseDto> enroll(@PathVariable String id) {
        return meetService.enroll(id).map(meet -> objectMapper.convertValue(meet, MeetResponseDto.class));
    }

    @PatchMapping("/{id}/checkin")
    @Tag(name = "checkin", description = "Allows a user to indicate if they finally attended a meeting")
    public Mono<MeetDto> checkin(@PathVariable String id) {
        return meetService.checkin(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElement(NoSuchElementException ex) {
        ApiError apiError = new ApiError(4040, "Meet not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(IntegrationError.class)
    public ResponseEntity<ApiError> handleIntegrationError(IntegrationError ex) {
        ApiError apiError = new ApiError(5000, "We are experiencing some problems, please retry in a few minutes.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(4000, "Bad request!.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(InvalidCheckinException.class)
    public ResponseEntity<ApiError> handleInvalidCheckinException(InvalidCheckinException ex) {
        ApiError apiError = new ApiError(4001, "You can't checkin at a meet that hasn't happened yet");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(InvalidEnrollException.class)
    public ResponseEntity<ApiError> handleInvalidEnrollException(InvalidEnrollException ex) {
        ApiError apiError = new ApiError(4002, "You can't enroll to a meet that happened");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
