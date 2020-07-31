package com.santander.birrameet.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequestDto {

    private String username;
    private String password;

    public SignUpRequestDto(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
