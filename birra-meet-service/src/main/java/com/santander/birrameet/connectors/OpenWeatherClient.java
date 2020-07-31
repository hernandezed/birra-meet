package com.santander.birrameet.connectors;

import com.santander.birrameet.connectors.config.OpenWeatherConfiguration;
import com.santander.birrameet.connectors.model.openWeather.OpenWeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "open-weather-client", url = "${clients.open-weather.base-url}", configuration = OpenWeatherConfiguration.class)
public interface OpenWeatherClient {

    @GetMapping(path = "/forecast/climate", produces = {"application/json"})
    OpenWeatherResponse getForecastForThirtyDays(@RequestParam Double lon, @RequestParam Double lat);
}
