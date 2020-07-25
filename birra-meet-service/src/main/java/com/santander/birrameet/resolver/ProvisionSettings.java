package com.santander.birrameet.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "birra-meet.provision-strategy")
@AllArgsConstructor
@Getter
public class ProvisionSettings {

    private Integer birrasPorCaja;
    private Set<CondicionSettings> condicion;
}
