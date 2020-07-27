package com.santander.birrameet.config;

import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.dto.MeetWithBeerBoxDto;
import com.santander.birrameet.response.MeetResponseDto;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfig {

    @Bean
    public MapperFacade mapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(MeetWithBeerBoxDto.class, MeetResponseDto.class);
        return mapperFactory.getMapperFacade();
    }
}
