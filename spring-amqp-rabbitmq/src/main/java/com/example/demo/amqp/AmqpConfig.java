package com.example.demo.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

public class AmqpConfig {

    private AmqpConfig() {}

    @Profile({"rabbitmq"})
    @Configuration
    static class RabbitMQConfig {

        public static final String SPRING_AMQP_QUEUE = "spring-amqp";

        /**
         * Create "spring-amqp" queue in RabbitMQ Broker.
         * RabbitMQ 브로커에 "spring-amqp" 큐 생성.
         */
        @Bean
        public Queue springAmqpQueue() {
            return new Queue(SPRING_AMQP_QUEUE);
        }

    }
}
