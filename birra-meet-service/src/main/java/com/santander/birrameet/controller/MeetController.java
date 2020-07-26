package com.santander.birrameet.controller;

import com.santander.birrameet.resolver.ProvisionResolver;
import com.santander.birrameet.response.LocationResponseDto;
import com.santander.birrameet.response.MeetResponseDto;
import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("meet")
public class MeetController {

    private final MeetService meetService;
    private final ProvisionResolver provisionResolver;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MeetResponseDto>> find(@PathVariable String id) {
        return meetService.findById(id).map(meet -> {
            Long boxes = provisionResolver.resolve(meet);
            return ResponseEntity.ok(new MeetResponseDto(meet.getId().toString(), meet.getTitle(), meet.getParticipants().size(),
                    meet.getDate(), new LocationResponseDto(meet.getLocation().getLongitude(), meet.getLocation().getLatitude()),
                    boxes));
        });
    }
}
