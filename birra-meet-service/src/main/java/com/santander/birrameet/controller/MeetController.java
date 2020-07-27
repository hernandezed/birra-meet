package com.santander.birrameet.controller;


import com.santander.birrameet.exceptions.IntegrationError;
import com.santander.birrameet.response.ApiError;
import com.santander.birrameet.response.MeetResponseDto;
import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
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
    private final MapperFacade mapperFacade;

    @GetMapping("/{id}")
    public Mono<MeetResponseDto> find(@PathVariable String id) {
        return meetService.findById(id).map(meet -> mapperFacade.map(meet, MeetResponseDto.class));

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


}
