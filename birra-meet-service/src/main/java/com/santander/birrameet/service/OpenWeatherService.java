package com.santander.birrameet.service;

import com.santander.birrameet.connectors.model.openWeather.Root;

public interface OpenWeatherService {
    Root getForecastForThirtyDays(Double lon, Double lat);
}
