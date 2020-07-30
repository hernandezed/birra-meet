package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.domain.Location;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.scheduler.GetFinalTemperatureForMeetScheduler;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ITGetFinalTemperatureForMeetScheduler extends BirraMeetApplicationTests {

    @Autowired
    private GetFinalTemperatureForMeetScheduler scheduler;

    @Test
    void execute_mustUpdateOnlyTodayMeets() {
        BirraMeetApplicationTests.wireMockServer.stubFor(get(urlEqualTo("/forecast/climate?lon=-50.0&lat=40.0&appid=" + apikey + "&units=metric"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response/openWeather_-50_40.json")));

        User admin = mongoTemplate.insert(new User(null, "moe", passwordEncoder.encode("123456"), true, List.of(Role.ROLE_ADMIN))).block();
        mongoTemplate.insertAll(IntStream.range(0, 36).mapToObj(num -> new Meet(null, "Meet " + num, admin.getId(), null, LocalDate.now().atStartOfDay().plusHours(num), new Location(-50d, 40d)))
                .collect(Collectors.toList())).collectList().block();
        scheduler.updateFinalTemperature();

        List<Meet> meets = mongoTemplate.findAll(Meet.class).collectList().block();
        assertThat(meets.stream().map(Meet::getTemperature).filter(Objects::nonNull)).hasSize(24);
    }


}
