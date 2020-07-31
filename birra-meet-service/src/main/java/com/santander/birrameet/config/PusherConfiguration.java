package com.santander.birrameet.config;

import com.pusher.rest.Pusher;
import com.santander.birrameet.config.settings.PusherSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class PusherConfiguration {
    @Bean
    public Pusher pusher(PusherSettings pusherSettings) {
        Pusher pusher = new Pusher(pusherSettings.getAppId(), pusherSettings.getKey(), pusherSettings.getSecret());
        pusher.setCluster(pusherSettings.getCluster());
        pusher.setEncrypted(pusherSettings.getEncrypted());
        return pusher;
    }
}
