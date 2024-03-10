package org.example.cloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {
    @RabbitListener(queues = "create-queue")
    public void ping(String payload) {
        log.info("Received message with payload: {}", payload);
    }
}
