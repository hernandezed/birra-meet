package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.domain.Location;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ITMeetControllerGetMeet extends BirraMeetApplicationTests {

    private Meet meet;
    private Meet meetOpenWeatherApiError;

    @BeforeAll
    void beforeAll() {
        User admin = new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN));
        Set<User> springfield = IntStream.range(0, 50).mapToObj(val -> new User(null, "SpringfieldCitizen" + val, passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER))).collect(Collectors.toSet());
        User savedAdmin = mongoTemplate.insert(admin).block();
        Set<ObjectId> participants = Set.copyOf(Objects.requireNonNull(mongoTemplate.insertAll(springfield).map(User::getId).collect(Collectors.toList()).block()));
        this.meet = mongoTemplate.insert(new Meet(null, "Veamos Jamas Termina", savedAdmin.getId(), participants, LocalDateTime.of(2020, 8, 4, 20, 00, 00, 00), new Location(-50d, 40d))).block();
        this.meetOpenWeatherApiError = mongoTemplate.insert(new Meet(null, "Veamos Jamas Termina", savedAdmin.getId(), participants, LocalDateTime.of(2020, 8, 4, 20, 00, 00, 00), new Location(-60d, 40d))).block();
    }

    @Test
    void getMeet_withMeetWith50Participants_andDayWithMore25Degree_withAdminUser_mustReturnMeetWith17Boxes() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(meet.getId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.boxes").value(Matchers.equalTo(17))
                .jsonPath("$.temperature").value(Matchers.equalTo(28.17d))
                .jsonPath("$.id").exists();
    }

    @Test
    void getMeet_withMeetWith50Participants_andDayWithMore25Degree_withRegularUser_mustReturnMeetWithoutBoxes() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("SpringfieldCitizen1", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(meet.getId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.boxes").doesNotExist()
                .jsonPath("$.temperature").value(Matchers.equalTo(28.17d))
                .jsonPath("$.id").exists();
    }

    @Test
    void getMeet_withMeetWith50Participants_andDayWithMore25Degree_withoutUser_mustReturnMeetWithoutBoxes() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(meet.getId()))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.boxes").doesNotExist()
                .jsonPath("$.temperature").value(Matchers.equalTo(28.17d))
                .jsonPath("$.id").exists();
    }

    @Test
    void getMeet_withInvalidId_mustThrowError() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(ObjectId.get().toString()))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Meet not found");
    }

    @Test
    void getMeet_withApiError_mustThrowError() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(meetOpenWeatherApiError.getId().toString()))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.message").isEqualTo("We are experiencing some problems, please retry in a few minutes.");
    }

}
