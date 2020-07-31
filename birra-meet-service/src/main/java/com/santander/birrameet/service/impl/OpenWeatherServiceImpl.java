package com.santander.birrameet.service.impl;

import com.santander.birrameet.connectors.OpenWeatherClient;
import com.santander.birrameet.connectors.model.openWeather.OpenWeatherResponse;
import com.santander.birrameet.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpenWeatherServiceImpl implements OpenWeatherService {

    private final OpenWeatherClient openWeatherClient;

    @Override
    @Cacheable("open-weather-get-forecast-for-thirty-days")
    public OpenWeatherResponse getForecastForThirtyDays(Double lon, Double lat) {
        return openWeatherClient.getForecastForThirtyDays(lon, lat);
    }
}
