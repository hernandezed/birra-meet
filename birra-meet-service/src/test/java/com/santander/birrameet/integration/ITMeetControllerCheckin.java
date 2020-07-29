package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.domain.Assistant;
import com.santander.birrameet.domain.Location;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.assertj.core.util.Sets;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ITMeetControllerCheckin extends BirraMeetApplicationTests {
    User admin;
    Set<Assistant> assistants;

    @BeforeAll
    void initContext() {
        BirraMeetApplicationTests.wireMockServer.resetAll();
        BirraMeetApplicationTests.wireMockServer.stubFor(get(urlEqualTo("/forecast/climate?lon=-50.0&lat=40.0&appid=" + apikey + "&units=metric"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response/openWeather_-50_40.json")));

        BirraMeetApplicationTests.wireMockServer.stubFor(get(urlEqualTo("/forecast/climate?lon=-60.0&lat=40.0&appid=" + apikey + "&units=metric"))
                .willReturn(aResponse().withStatus(500)));
        admin = mongoTemplate.insert(new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN))).block();
        Set<User> springfield = IntStream.range(0, 50).mapToObj(val -> new User(null, "SpringfieldCitizen" + val,
                passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER)))
                .collect(Collectors.toSet());
        assistants = Set.copyOf(Objects.requireNonNull(mongoTemplate.insertAll(springfield)
                .map(User::getId).map((id) -> new Assistant(id, false)).collect(Collectors.toList()).block()));
    }

    @Test
    void checkIn_withLoggedUser_withValidMeet_userEnrolled_mustSetTrueToAssistance() {
        Meet meet = mongoTemplate.insert(new Meet(null, "DDD", admin.getId(), assistants,
                LocalDate.now().plusMonths(5).atStartOfDay(), new Location(-50d, 40d))).block();

        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("SpringfieldCitizen10", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.patch().uri("/meet/" + meet.getId().toString() + "/checkin")
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        Meet meetAfterCheckIn = mongoTemplate.findById(meet.getId(), Meet.class).block();

        assertThat(meetAfterCheckIn.getParticipants().stream().filter(Assistant::isAssist)).hasSize(1);
    }

    @Test
    void checkIn_withLoggedAdminUser_withValidMeet_userEnrolled_mustSetTrueToAssistance() {
        Set<Assistant> assistantsToMeet = Sets.newHashSet(assistants);
        assistantsToMeet.add(new Assistant(admin.getId(), false));

        Meet meet = mongoTemplate.insert(new Meet(null, "DDD2", admin.getId(), assistantsToMeet,
                LocalDate.now().plusMonths(6).atStartOfDay(), new Location(-50d, 40d))).block();

        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.patch().uri("/meet/" + meet.getId().toString() + "/checkin")
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        Meet meetAfterCheckIn = mongoTemplate.findById(meet.getId(), Meet.class).block();

        assertThat(meetAfterCheckIn.getParticipants().stream().filter(Assistant::isAssist)).hasSize(1);
    }

    @Test
    void checkIn_withNotLoggedUser_withValidMeet_userEnrolled_mustSetTrueToAssistance() {
        webTestClient.patch().uri("/meet/123/checkin")
                .header("Authorization", "Bearer " + "1234")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void checkIn_withLoggedUser_withInvalidMeet_mustSetTrueToAssistance() {
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("SpringfieldCitizen10", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();
        webTestClient.patch().uri("/meet/" + new ObjectId() + "/checkin")
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }


}
