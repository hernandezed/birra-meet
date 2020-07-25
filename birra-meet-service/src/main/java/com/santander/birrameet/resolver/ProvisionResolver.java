package com.santander.birrameet.resolver;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProvisionResolver {

    private ProvisionSettings provisionSettings;

    public Long resolve(int asistentes, double temperatura) {
        CondicionSettings condicionSettings = provisionSettings.getCondicion().stream()
                .filter(condicion -> condicion.getDesde() <= temperatura && condicion.getHasta() >= temperatura).findFirst()
                .get();
        return ((Double) Math.ceil((asistentes * condicionSettings.getBirras()) / provisionSettings.getBirrasPorCaja())).longValue();
    }

}
