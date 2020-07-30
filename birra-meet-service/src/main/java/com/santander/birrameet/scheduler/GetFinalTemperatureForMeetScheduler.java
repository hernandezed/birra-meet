package com.santander.birrameet.scheduler;


import com.santander.birrameet.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFinalTemperatureForMeetScheduler {

    private final MeetService meetService;

    @Scheduled(cron = "0 0 0 ? * * *")
    public void updateFinalTemperature() {
        meetService.updateTemperature().blockLast();
    }


}
