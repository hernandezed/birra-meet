package com.santander.birrameet.resolver;

import com.santander.birrameet.connectors.OpenWeatherClient;
import com.santander.birrameet.connectors.model.openWeather.Root;
import com.santander.birrameet.domain.Meet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProvisionResolver {

    private final OpenWeatherClient openWeatherClient;
    private final ProvisionSettings provisionSettings;

    public Long resolve(Meet meet) {
        Root root = openWeatherClient.getForecastForThirtyDays(meet.getLocation().getLongitude(), meet.getLocation().getLatitude());
        double temperature = root.getList().stream().filter(whetherList -> LocalDate.ofEpochDay(whetherList.getDt() / 86400).equals(meet.getDate().toLocalDate()))
                .findFirst().get().getTemp().getMax();
        CondicionSettings condicionSettings = provisionSettings.getCondiciones().stream()
                .filter(condicion -> condicion.getDesde() <= temperature && condicion.getHasta() >= temperature).findFirst()
                .get();
        return ((Double) Math.ceil((meet.getParticipants().size() * condicionSettings.getBirras()) / provisionSettings.getBirrasPorCaja()))
                .longValue();
    }

}
