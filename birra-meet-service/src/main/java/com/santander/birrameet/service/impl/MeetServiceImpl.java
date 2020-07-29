package com.santander.birrameet.service.impl;

import com.santander.birrameet.connectors.OpenWeatherClient;
import com.santander.birrameet.connectors.model.openWeather.Root;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.dto.MeetDto;
import com.santander.birrameet.exceptions.IntegrationError;
import com.santander.birrameet.exceptions.InvalidCheckinException;
import com.santander.birrameet.exceptions.InvalidEnrollException;
import com.santander.birrameet.repository.MeetRepository;
import com.santander.birrameet.repository.UserRepository;
import com.santander.birrameet.resolver.ProvisionResolver;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.security.model.User;
import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final ProvisionResolver provisionResolver;
    private final OpenWeatherClient openWeatherClient;
    private final UserRepository userRepository;

    @Override
    public Mono<MeetDto> findById(String id) {
        return meetRepository.findById(new ObjectId(id)).flatMap(meet -> ReactiveSecurityContextHolder
                .getContext().flatMap(securityContext -> createMeetDto(meet, securityContext))
                .switchIfEmpty(createMeetDto(meet, null)))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NoSuchElementException())));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<MeetDto> create(Meet meet) {
        validate(meet);
        return ReactiveSecurityContextHolder
                .getContext().map(SecurityContext::getAuthentication)
                .map(Authentication::getDetails)
                .cast(User.class)
                .map(User::getId)
                .flatMap(userId -> {
                    meet.withCreator(userId);
                    return meetRepository.insert(meet).flatMap(createdMeet -> createMeetWithBeerBoxDto(meet, null,
                            null));
                });
    }

    @Override
    public Mono<MeetDto> enroll(String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getDetails)
                .cast(User.class)
                .map(User::getId)
                .flatMap(userId -> meetRepository.findById(new ObjectId(id)).flatMap(meet -> {
                    if (LocalDateTime.now().isAfter(meet.getDate())) {
                        throw new InvalidEnrollException();
                    }
                    meet.addParticipant(userId);
                    return meetRepository.save(meet).flatMap(updated -> ReactiveSecurityContextHolder.getContext()
                            .flatMap(context -> createMeetDto(updated, context)));
                })
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new NoSuchElementException()))));
    }

    @Override
    public Mono<MeetDto> checkin(String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getDetails)
                .cast(User.class)
                .map(User::getId)
                .flatMap(userId -> meetRepository.findById(new ObjectId(id)).flatMap(meet -> {
                    if (LocalDateTime.now().isBefore(meet.getDate())) {
                        throw new InvalidCheckinException();
                    }
                    meet.getParticipants().stream().filter(assistant -> assistant.getUserId().equals(userId)).findFirst().get().assist();
                    return meetRepository.save(meet).flatMap(updated -> ReactiveSecurityContextHolder.getContext()
                            .flatMap(context -> createMeetDto(updated, context)));
                }))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NoSuchElementException())));
    }

    private void validate(Meet meet) {
        if (StringUtils.isEmpty(meet.getTitle()) || meet.getDate() == null || meet.getLocation() == null || LocalDateTime.now().compareTo(meet.getDate()) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private Mono<MeetDto> createMeetDto(Meet meet, SecurityContext securityContext) {
        Authentication authentication = Optional.ofNullable(securityContext).map(SecurityContext::getAuthentication).orElse(null);
        Long boxes = null;
        Double temperature = null;
        if (LocalDateTime.now().isBefore(meet.getDate()) && LocalDateTime.now().plusMonths(1).isAfter(meet.getDate())) {
            temperature = getTemperature(meet);
            if (Objects.nonNull(authentication) && authentication.getAuthorities().stream().map(auth -> (SimpleGrantedAuthority) auth).map(SimpleGrantedAuthority::getAuthority)
                    .anyMatch(auth -> auth.equals(Role.ROLE_ADMIN.name()))) {
                boxes = provisionResolver.resolve(meet, temperature);
            }
        }
        return createMeetWithBeerBoxDto(meet, boxes, temperature);
    }

    private Mono<MeetDto> createMeetWithBeerBoxDto(Meet meet, Long boxes, Double temperature) {
        return userRepository.findById(meet.getCreator()).map(user -> new MeetDto(meet.getId().toString(), meet.getTitle(), user.getUsername(), meet.getParticipants().size(), meet.getDate(), meet.getLocation(), boxes, temperature));


    }

    private double getTemperature(Meet meet) {
        try {
            Root root = openWeatherClient.getForecastForThirtyDays(meet.getLocation().getLongitude(), meet.getLocation().getLatitude());
            return root.getList().stream().filter(whetherList -> LocalDate.ofEpochDay(whetherList.getDt() / 86400).equals(meet.getDate().toLocalDate()))
                    .findFirst().get().getTemp().getMax();
        } catch (Exception e) {
            throw new IntegrationError();
        }

    }
}
