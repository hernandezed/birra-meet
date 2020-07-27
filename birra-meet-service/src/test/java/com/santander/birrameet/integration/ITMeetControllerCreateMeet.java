package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.commons.LocationApiDto;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.request.MeetCreateRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.List;

public class ITMeetControllerCreateMeet extends BirraMeetApplicationTests {

    @Test
    void createMeet_withValidRequest_mustReturnMeetCreated() {
        String creatorId = mongoTemplate.insert(new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN))).block().getId().toString();
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("BirraJs", LocalDateTime.now(), new LocationApiDto(100.90d, -50d));
        WebTestClient.ResponseSpec responseSpec = webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        responseSpec
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.title").isEqualTo(meetCreateDto.getTitle())
                .jsonPath("$.location.longitude").isEqualTo(meetCreateDto.getLocation().getLongitude())
                .jsonPath("$.location.latitude").isEqualTo(meetCreateDto.getLocation().getLatitude())
                .jsonPath("$.creator").isEqualTo("moe");
    }


}