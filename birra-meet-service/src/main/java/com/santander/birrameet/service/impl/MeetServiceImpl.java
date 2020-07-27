package com.santander.birrameet.service.impl;

import com.santander.birrameet.connectors.OpenWeatherClient;
import com.santander.birrameet.connectors.model.openWeather.Root;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.dto.MeetWithBeerBoxDto;
import com.santander.birrameet.repository.MeetRepository;
import com.santander.birrameet.resolver.ProvisionResolver;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final ProvisionResolver provisionResolver;
    private final OpenWeatherClient openWeatherClient;

    @Override
    public Mono<MeetWithBeerBoxDto> findById(String id) {
        return meetRepository.findById(new ObjectId(id)).flatMap(meet -> ReactiveSecurityContextHolder
                .getContext().flatMap(securityContext -> getMeet(meet, securityContext))
                .switchIfEmpty(getMeet(meet, null)));
    }

    private Mono<MeetWithBeerBoxDto> getMeet(Meet meet, SecurityContext securityContext) {
        Authentication authentication = Optional.ofNullable(securityContext).map(SecurityContext::getAuthentication).orElse(null);
        Long boxes = null;
        double temperature = getTemperature(meet);
        if (Objects.nonNull(authentication) && authentication.getAuthorities().stream().map(auth -> (SimpleGrantedAuthority) auth).map(SimpleGrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(Role.ROLE_ADMIN.name()))) {
            boxes = provisionResolver.resolve(meet, temperature);
        }
        return Mono.just(createMeetWithBeerBoxDto(meet, boxes, temperature));
    }

    private MeetWithBeerBoxDto createMeetWithBeerBoxDto(Meet meet, Long boxes, Double temperature) {
        return new MeetWithBeerBoxDto(meet.getId().toString(), meet.getTitle(), meet.getCreator(), meet.getParticipants().size(), meet.getDate(), meet.getLocation(), boxes, temperature);
    }

    private double getTemperature(Meet meet) {
        Root root = openWeatherClient.getForecastForThirtyDays(meet.getLocation().getLongitude(), meet.getLocation().getLatitude());
        return root.getList().stream().filter(whetherList -> LocalDate.ofEpochDay(whetherList.getDt() / 86400).equals(meet.getDate().toLocalDate()))
                .findFirst().get().getTemp().getMax();
    }
}