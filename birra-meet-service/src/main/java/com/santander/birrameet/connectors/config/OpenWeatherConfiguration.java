package com.santander.birrameet.connectors.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenWeatherConfiguration {
    @Value("${clients.open-weather.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.query("appid", apiKey).query("units", "metric");
    }
}
