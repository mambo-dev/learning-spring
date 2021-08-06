package com.example.demo.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.demo.amqp.AmqpConfig.RabbitMQConfig.SPRING_AMQP_QUEUE;

@Slf4j
@Service
public class DefaultProducer {

    private final RabbitTemplate rabbitTemplate;

    public DefaultProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 100, initialDelay = 500)
    public void publish() {
        String message = "uuid:" + UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(SPRING_AMQP_QUEUE, message);
        log.info("publish {}", message);
    }
}
