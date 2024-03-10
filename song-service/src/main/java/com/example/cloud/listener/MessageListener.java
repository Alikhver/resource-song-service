package com.example.cloud.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    @RabbitListener(queues = "delete-queue")
    public void receiveMessage(String payload) {
        log.info("Received message with payload: {}", payload);
    }
}
