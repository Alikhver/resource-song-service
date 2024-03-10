package com.example.cloud.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagePublisherImpl implements MessagePublisher {
    private final RabbitTemplate rabbitTemplate;
    @Override
    public void postCreateMessage(Object message) {
        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend("my-exchange", "my.routing.create.key.1", message);
    }

    @Override
    public void postDeleteMessage(Object message) {
        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend("my-exchange", "my.routing.delete.key.2", message);
    }
}
