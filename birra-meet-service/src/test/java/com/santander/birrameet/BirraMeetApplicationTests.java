package com.santander.birrameet;

import com.santander.birrameet.extension.GlobalTestContainersExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

@ExtendWith(value = {GlobalTestContainersExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public abstract class BirraMeetApplicationTests {

    @Value("${local.server.port}")
    private Integer port;

    private static final Integer REDIS_PORT = 6379;
    @Container
    public static final GenericContainer MONGODB_CONTAINER = new GenericContainer("mongo")
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "")
            .withEnv("MONGO_INITDB_DATABASE", "birra-meet")
            .waitingFor(Wait.forListeningPort());
    @Container
    public static final GenericContainer REDIS_CONTAINER =
            new GenericContainer("redis")
                    .withExposedPorts(REDIS_PORT)
                    .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void setUpMockedProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", () -> "mongodb://localhost:" + MONGODB_CONTAINER.getMappedPort(27017) + "/birra-meet");
        dynamicPropertyRegistry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT));
    }


}
