package com.santander.birrameet;

import com.santander.birrameet.extension.GlobalTestContainersExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

@ExtendWith(value = {GlobalTestContainersExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BirraMeetApplicationTests {
    private static final Integer REDIS_PORT = 6379;
    @Container
    public static final GenericContainer MYSQL_CONTAINER = new MySQLContainer().withDatabaseName("birra-meet").waitingFor(Wait.forListeningPort());
    @Container
    public static final GenericContainer REDIS_CONTAINER =
            new GenericContainer("redis")
                    .withExposedPorts(REDIS_PORT)
                    .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void setUpMockedProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        final MySQLContainer mySQLContainer = (MySQLContainer) BirraMeetApplicationTests.MYSQL_CONTAINER;
        dynamicPropertyRegistry.add("spring.r2dbc.url", () -> "r2dbc:pool:mysql://localhost:" + mySQLContainer.getMappedPort(3306) + "/" + mySQLContainer.getDatabaseName());
        dynamicPropertyRegistry.add("spring.r2dbc.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.r2dbc.password", mySQLContainer::getPassword);
       // dynamicPropertyRegistry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        dynamicPropertyRegistry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT));
    }

    @Test
    void contextLoads() {
    }

}
