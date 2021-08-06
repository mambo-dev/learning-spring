package com.example.demo.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.example.demo.amqp.AmqpConfig.RabbitMQConfig.SPRING_AMQP_QUEUE;

@Slf4j
@Service
public class DefaultConsumer {

    @RabbitListener(queues = {SPRING_AMQP_QUEUE}, concurrency = "3-5")
    public void consume(Message message) {
        log.info("consume {}", new String(message.getBody()));
    }

    @RabbitListener(
        concurrency = "3-5",
        bindings = {
            @QueueBinding(
                value = @Queue(name = SPRING_AMQP_QUEUE),
                exchange = @Exchange(name = "amq.direct")
            )
        }
    )
    public void receive(Message message) {
        log.info("receive {}", new String(message.getBody()));
    }

}
