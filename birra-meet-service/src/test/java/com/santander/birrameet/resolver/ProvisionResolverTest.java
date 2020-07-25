package com.santander.birrameet.resolver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProvisionResolverTest {

    private ProvisionResolver provisionResolver;

    @BeforeAll
    public void setUp() {
        Set<CondicionSettings> condiciones = new HashSet<>();
        condiciones.add(new CondicionSettings(20d, 24d, 1d));
        condiciones.add(new CondicionSettings(null, 19d, 0.75d));
        condiciones.add(new CondicionSettings(25d, null, 2d));
        ProvisionSettings provisionSettings = new ProvisionSettings(6, condiciones);
        provisionResolver = new ProvisionResolver(provisionSettings);
    }

    @Test
    void resolve_givenConditions_withTemp20_and50Participants_mustReturn9BeerBoxes() {
        assertThat(provisionResolver.resolve(50, 20)).isEqualTo(9);
    }

    @Test
    void resolve_givenConditions_withTemp30_and50Participants_mustReturn17BeerBoxes() {
        assertThat(provisionResolver.resolve(50, 30)).isEqualTo(17);
    }
}
