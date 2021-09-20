package com.example.rabbitProducer.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(initializers = {EventIT.Initializer.class})
@Testcontainers
public class EventIT {

    @Container
    private static final RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672).waitingFor(Wait.forLogMessage(".*Server startup complete.*",1));

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @DisplayName("MessagePost")
    @Test
    public void testProduceMessage() {
        Assertions.assertTrue(rabbit.isRunning());
        rabbitTemplate.convertAndSend("New message");
    }


    public static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.out.println("Initialize Padma");
            TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672)
            ).applyTo(configurableApplicationContext);
        }
    }
}
