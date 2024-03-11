package org.example.cloud.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.example.cloud.service.ProcessorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {
    private final ProcessorService processorService;

    @RabbitListener(queues = "create-queue")
    public void receiveMessage(Long payload) throws TikaException, IOException, SAXException {
        log.info("Received message with payload: {}", payload);
        processorService.parseAndCreateMetadata(payload);
    }
}
