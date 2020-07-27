package com.santander.birrameet.controller;


import com.santander.birrameet.resolver.ProvisionResolver;
import com.santander.birrameet.response.MeetResponseDto;
import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
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
    private final MapperFacade mapperFacade;

    @GetMapping("/{id}")
    public Mono<MeetResponseDto> find(@PathVariable String id) {
        return meetService.findById(id).map(meet -> mapperFacade.map(meet, MeetResponseDto.class));
    }
}
