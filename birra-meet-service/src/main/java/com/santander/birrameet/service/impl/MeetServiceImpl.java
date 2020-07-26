package com.santander.birrameet.service.impl;

import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.repository.MeetRepository;
import com.santander.birrameet.service.MeetService;
import com.santander.birrameet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final UserService userService;

    @Override
    public Mono<Meet> findById(String id) {
        return meetRepository.findById(new ObjectId(id));
    }
}
