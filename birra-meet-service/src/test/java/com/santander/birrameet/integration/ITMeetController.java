package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.domain.Location;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ITMeetController extends BirraMeetApplicationTests {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getNeededBeers_withMeetWith50Participants_andDayWith30Degree_mustReturn17Boxes() {
        User admin = new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN));
        Set<User> springfield = IntStream.range(0, 50).mapToObj(val -> new User(null, "SpringfieldCitizen" + val, passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER))).collect(Collectors.toSet());
        User savedAdmin = mongoTemplate.insert(admin).block();
        Set<User> participants = Set.copyOf(mongoTemplate.insertAll(springfield).collect(Collectors.toList()).block());
        Meet meet = new Meet(null, "Veamos Jamas Termina", savedAdmin, participants, ZonedDateTime.of(2020, 10, 20, 20, 00, 00, 00, ZoneId.of("America/Argentina/Buenos_Aires")), new Location(-50, 40));
        mongoTemplate.insert(meet).block();
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/meet/{id}")
                .build(meet.getId()))
                .exchange()
                .expectBody().jsonPath("$.boxes").value(Matchers.equalTo(17));

    }
}
