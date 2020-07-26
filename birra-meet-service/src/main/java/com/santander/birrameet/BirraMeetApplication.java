package com.santander.birrameet;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BirraMeetApplication {

    public static void main(String[] args) {
        SpringApplication.run(BirraMeetApplication.class, args);
    }

}
