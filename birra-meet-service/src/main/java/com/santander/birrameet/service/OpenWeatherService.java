package com.santander.birrameet.service;

import com.santander.birrameet.connectors.model.openWeather.OpenWeatherResponse;

public interface OpenWeatherService {
    OpenWeatherResponse getForecastForThirtyDays(Double lon, Double lat);
}
