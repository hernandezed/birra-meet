package com.santander.birrameet.integration;

import com.santander.birrameet.BirraMeetApplicationTests;
import com.santander.birrameet.request.SignUpRequestDto;
import com.santander.birrameet.security.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

public class ITAuthenticationControllerSignUp extends BirraMeetApplicationTests {

    @Test
    void signUp_withValidUser_returnCreated_persistUserWithRoleUserAnd() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("new-user", "123456");

        webTestClient.post()
                .uri("/auth/sign-up")
                .body(BodyInserters.fromValue(signUpRequestDto))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED);

        User user = mongoTemplate.findOne(Query.query(Criteria.where("username").is(signUpRequestDto.getUsername())), User.class).block();
        assertThat(user.getUsername()).isEqualTo(signUpRequestDto.getUsername());
        assertThat(passwordEncoder.matches(signUpRequestDto.getPassword(), user.getPassword())).isTrue();
    }
}
