package com.example.resourcesongservice.web;

import com.example.resourcesongservice.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

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
    public Long upload(InputStream data) throws IOException, TikaException, SAXException {
        return resourceService.createResource(data);
    }

    @GetMapping(value = "/{id}")
    public byte[] get(@PathVariable Long id) {
        return resourceService.getResourceById(id);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteByIds(@RequestParam String ids) {
        var deletedIds = resourceService.deleteByIds(ids);
        return ResponseEntity.ok().body(Map.of("ids", deletedIds));
    }
}
