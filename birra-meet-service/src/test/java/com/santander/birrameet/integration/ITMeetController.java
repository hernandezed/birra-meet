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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ITMeetController extends BirraMeetApplicationTests {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebTestClient webTestClient;
    @Value("${clients.open-weather.api-key}")
    private String apikey;

    @BeforeAll
    void beforeAll() {
        BirraMeetApplicationTests.wireMockServer.stubFor(get(urlEqualTo("/forecast/climate?lon=-50.0&lat=40.0&appid=" + apikey+ "&units=metric"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response/openWeather_-50_40.json")));
    }

    @Test
    void getNeededBeers_withMeetWith50Participants_andDayWith30Degree_mustReturn17Boxes() {


        User admin = new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN));
        Set<User> springfield = IntStream.range(0, 50).mapToObj(val -> new User(null, "SpringfieldCitizen" + val, passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER))).collect(Collectors.toSet());
        User savedAdmin = mongoTemplate.insert(admin).block();
        Set<ObjectId> participants = Set.copyOf(Objects.requireNonNull(mongoTemplate.insertAll(springfield).map(User::getId).collect(Collectors.toList()).block()));
        Meet meet = new Meet(null, "Veamos Jamas Termina", savedAdmin.getId(), participants, LocalDateTime.of(2020, 8, 4, 20, 00, 00, 00), new Location(-50d, 40d));

        mongoTemplate.insert(meet).block();
        LoginResponseDto loginResponseDto = webTestClient.post().uri("/auth/login")
                .body(BodyInserters.fromValue(new LoginRequestDto("moe", "123456")))
                .exchange().returnResult(LoginResponseDto.class)
                .getResponseBody().blockLast();


        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(meet.getId()))
                .header("Authorization", "Bearer " + loginResponseDto.getToken())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.boxes").value(Matchers.equalTo(17));

    }
}
