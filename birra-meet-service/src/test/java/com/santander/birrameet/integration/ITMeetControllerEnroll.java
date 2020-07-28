package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.domain.Location;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ITMeetControllerEnroll extends BirraMeetApplicationTests {

    @BeforeAll
    void createContext() {
        mongoTemplate.insert(new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN))).block();
        mongoTemplate.insert(new User(null, "user_comun", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER))).block();
    }

    @Test
    void enroll_withLoggedUser_mustReturnMeetWithUserEnrolled() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("user_comun", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();

        User user = mongoTemplate.find(Query.query(where("username").is("moe")), User.class).blockFirst();

        Meet meet = mongoTemplate.insert(new Meet(null, "BirraJs", user.getId(), null, LocalDateTime.now().plusDays(3), new Location(-50d, 40d))).block();

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/meet/{id}/enroll")
                        .build(meet.getId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectBody()
                .jsonPath("$.participants").isEqualTo(1);
    }

    @Test
    void enroll_withLoggedAdminUser_mustReturnMeetWithUserEnrolled() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();

        User user = mongoTemplate.find(Query.query(where("username").is("moe")), User.class).blockFirst();

        Meet meet = mongoTemplate.insert(new Meet(null, "BirraJs", user.getId(), null, LocalDateTime.now().plusDays(3), new Location(-50d, 40d))).block();

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/meet/{id}/enroll")
                        .build(meet.getId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectBody()
                .jsonPath("$.participants").isEqualTo(1);
    }

    @Test
    void enroll_withoutLoggedUser_throwError() {
        User user = mongoTemplate.find(Query.query(where("username").is("moe")), User.class).blockFirst();

        Meet meet = mongoTemplate.insert(new Meet(null, "BirraJs", user.getId(), null, LocalDateTime.now().plusDays(3), new Location(-50d, 40d))).block();

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/meet/{id}/enroll")
                        .build(meet.getId()))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void enroll_withInvalidMeetId_throwError() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("user_comun", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/meet/{id}/enroll")
                        .build(new ObjectId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    void enroll_withErrorInWeatherService_throwError() {
        User user = mongoTemplate.find(Query.query(where("username").is("moe")), User.class).blockFirst();
        Meet meet = mongoTemplate.insert(new Meet(null, "BirraJs", user.getId(), null, LocalDateTime.now().plusDays(3), new Location(-60d, 40d))).block();
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("user_comun", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path("/meet/{id}/enroll")
                        .build(meet.getId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
