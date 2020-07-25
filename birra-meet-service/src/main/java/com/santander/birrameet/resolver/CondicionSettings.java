package com.santander.birrameet.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class CondicionSettings {

    private Double desde;
    private Double hasta;
    private Double birras;

    public Double getDesde() {
        return Optional.ofNullable(desde).orElse(Double.MIN_VALUE);
    }

    public Double getHasta() {
        return Optional.ofNullable(hasta).orElse(Double.MAX_VALUE);
    }
}
