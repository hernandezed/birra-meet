package com.santander.birrameet.resolver;

import com.santander.birrameet.domain.Meet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProvisionResolver {
    private final ProvisionSettings provisionSettings;

    public Long resolve(Meet meet, double temperature) {
        CondicionSettings condicionSettings = provisionSettings.getCondiciones().stream()
                .filter(condicion -> condicion.getDesde() <= temperature && condicion.getHasta() >= temperature).findFirst()
                .get();
        return ((Double) Math.ceil((meet.getParticipants().size() * condicionSettings.getBirras()) / provisionSettings.getBirrasPorCaja()))
                .longValue();
    }

}
