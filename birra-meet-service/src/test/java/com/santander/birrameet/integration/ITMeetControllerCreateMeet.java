package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.commons.LocationApiDto;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.request.MeetCreateRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.List;

public class ITMeetControllerCreateMeet extends BirraMeetApplicationTests {

    @BeforeAll
    void createContext() {
        mongoTemplate.insert(new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN))).block();
        mongoTemplate.insert(new User(null, "user_comun", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER))).block();
    }

    @Test
    void createMeet_withValidRequest_withLoggedAdminUser_mustReturnMeetCreated() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("BirraJs", LocalDateTime.now().plusDays(3), new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.title").isEqualTo(meetCreateDto.getTitle())
                .jsonPath("$.location.longitude").isEqualTo(meetCreateDto.getLocation().getLongitude())
                .jsonPath("$.location.latitude").isEqualTo(meetCreateDto.getLocation().getLatitude())
                .jsonPath("$.creator").isEqualTo("moe");
    }

    @Test
    void createMeet_withValidRequest_withoutLoggedUser_mustThrowError() {
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("BirraJs", LocalDateTime.now().plusDays(3).plusDays(3), new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createMeet_withValidRequest_withLoggedUser_mustThrowError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("user_comun", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("BirraJs", LocalDateTime.now().plusDays(3), new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void createMeet_withInvalidRequestTitle_withLoggedAdminUser_mustThrowError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto(null, LocalDateTime.now().plusDays(3), new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createMeet_withInvalidRequestEmptyTitle_withLoggedAdminUser_mustThrowError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("", LocalDateTime.now().plusDays(3), new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createMeet_withInvalidDateRequest_withLoggedAdminUser_mustThrowError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("Title", null, new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createMeet_withInvalidLocationRequest_withLoggedAdminUser_mustThrowError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("Title", LocalDateTime.now().plusDays(3), null);
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createMeet_withInvalidDateBefore_withLoggedAdminUser_mustThrowError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        MeetCreateRequestDto meetCreateDto = new MeetCreateRequestDto("Title", LocalDateTime.now().minusDays(3),  new LocationApiDto(100.90d, -50d));
        webTestClient.post().uri("/meet")
                .body(BodyInserters.fromValue(meetCreateDto))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
