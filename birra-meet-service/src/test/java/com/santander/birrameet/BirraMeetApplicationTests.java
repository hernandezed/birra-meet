package com.santander.birrameet;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.santander.birrameet.domain.Meet;
import com.santander.birrameet.extension.GlobalTestContainersExtension;
import com.santander.birrameet.security.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@ExtendWith(value = {GlobalTestContainersExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BirraMeetApplicationTests {

    @Value("${local.server.port}")
    private Integer port;
    @Value("${clients.open-weather.api-key}")
    private String apikey;
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected ReactiveMongoTemplate mongoTemplate;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    public static WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());

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
        wireMockServer.start();
        dynamicPropertyRegistry.add("clients.open-weather.base-url", () -> "localhost:" + wireMockServer.port());
    }

    @BeforeAll
    void initContext() {
        BirraMeetApplicationTests.wireMockServer.stubFor(get(urlEqualTo("/forecast/climate?lon=-50.0&lat=40.0&appid=" + apikey + "&units=metric"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response/openWeather_-50_40.json")));

        BirraMeetApplicationTests.wireMockServer.stubFor(get(urlEqualTo("/forecast/climate?lon=-60.0&lat=40.0&appid=" + apikey + "&units=metric"))
                .willReturn(aResponse().withStatus(500)));

    }

    @AfterAll
    public void cleanUpContext() {
        mongoTemplate.dropCollection(Meet.class);
        mongoTemplate.dropCollection(User.class);
    }


}
