package com.example.cloud.web;

import com.example.cloud.service.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<Map<String, Long>> upload(InputStream data) throws IOException {
        log.info("ResourceController upload invoked");
        Long response = resourceService.createResource(data);
        return ResponseEntity.ok().body(Map.of("id", response));
    }

    @GetMapping(value = "/{id}")
    public byte[] get(@PathVariable Long id) {
        log.info("GetResource by id invoked with params: {}", id);
        byte[] resourceById = resourceService.getResourceById(id);
        return resourceById;
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteByIds(@RequestParam String ids) throws JsonProcessingException {
        log.info("Delete Resource by id invoked with params: {}", ids);
        var deletedIds = resourceService.deleteByIds(ids);
        return ResponseEntity.ok().body(Map.of("ids", deletedIds));
    }
}
