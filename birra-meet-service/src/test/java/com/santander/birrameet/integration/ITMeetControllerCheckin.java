package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.domain.Assistant;
import com.santander.birrameet.domain.Location;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.request.LoginRequestDto;
import com.santander.birrameet.response.LoginResponseDto;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ITMeetControllerCheckin extends BirraMeetApplicationTests {

    @Test
    void checkIn_withLoggedUser_withValidMeet_userEnrolled_mustSetTrueToAssistance() {
        User admin = new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN));
        Set<User> springfield = IntStream.range(0, 50).mapToObj(val -> new User(null, "SpringfieldCitizen" + val, passwordEncoder.encode("123456"), true, List.of(Role.ROLE_USER))).collect(Collectors.toSet());
        admin = mongoTemplate.insert(admin).block();
        Set<Assistant> participants = Set.copyOf(Objects.requireNonNull(mongoTemplate.insertAll(springfield).map(User::getId).map((id) -> new Assistant(id, false)).collect(Collectors.toList()).block()));
        Meet meet = new Meet(null, "DDD", admin.getId(), participants, LocalDate.now().plusMonths(5).atStartOfDay(), new Location(10d, -4d));
        meet = mongoTemplate.insert(meet).block();
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

}
