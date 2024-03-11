package com.example.cloud.listener;

import com.example.cloud.service.SongInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {
    private final ObjectMapper objectMapper;
    private final SongInfoService songInfoService;

    @RabbitListener(queues = "delete-queue")
    public void receiveMessage(String payload) throws JsonProcessingException {
        log.info("Received message with payload: {}", payload);
        List<Long> deletedResourceIds = objectMapper.readValue(payload, new TypeReference<List<Long>>() {});
        songInfoService.deleteSongInfos(deletedResourceIds);
    }
}
