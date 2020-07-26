package com.santander.birrameet.resolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "birra-meet.provision-strategy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvisionSettings {

    private Integer birrasPorCaja;
    private Set<CondicionSettings> condiciones;
}
