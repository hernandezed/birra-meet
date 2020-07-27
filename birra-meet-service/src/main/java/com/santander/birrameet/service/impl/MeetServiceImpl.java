package com.santander.birrameet.service.impl;

import com.santander.birrameet.dto.MeetWithBeerBoxDto;
import com.santander.birrameet.repository.MeetRepository;
import com.santander.birrameet.resolver.ProvisionResolver;
import com.santander.birrameet.security.model.Role;
import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final ProvisionResolver provisionResolver;
    private final MapperFacade mapperFacade;

    @Override
    public Mono<MeetWithBeerBoxDto> findById(String id) {
        return ReactiveSecurityContextHolder
                .getContext().flatMap(securityContext -> getMeet(id, securityContext));
    }

    private Mono<MeetWithBeerBoxDto> getMeet(String id, SecurityContext securityContext) {
        return meetRepository.findById(new ObjectId(id)).map(meet -> {
            Authentication authentication = securityContext.getAuthentication();
            Long boxes = null;
            if (authentication.getAuthorities().stream().map(auth -> (SimpleGrantedAuthority) auth).map(SimpleGrantedAuthority::getAuthority)
                    .anyMatch(auth -> auth.equals(Role.ROLE_ADMIN.name()))) {

                boxes = provisionResolver.resolve(meet);
            }
            return new MeetWithBeerBoxDto(meet.getId().toString(), meet.getTitle(), meet.getCreator(), meet.getParticipants().size(), meet.getDate(), meet.getLocation(), boxes);
        });


    }
}
