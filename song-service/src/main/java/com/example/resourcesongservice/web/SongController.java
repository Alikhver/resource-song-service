package com.example.resourcesongservice.web;

import com.example.resourcesongservice.dto.SongInfoDto;
import com.example.resourcesongservice.exception.SongInfoNotFoundException;
import com.example.resourcesongservice.service.SongInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/songs")
public class SongController {

    private final SongInfoService songInfoService;

    @PostMapping
    public ResponseEntity<Object> createNewMetadata(@RequestBody SongInfoDto songInfo) {
        log.info("CreateNewMetadata invoked with params: {}", songInfo);
        var createdSongInfoId = songInfoService.createNewMetadata(songInfo);
        return ResponseEntity.ok().body(Map.of("id", createdSongInfoId.toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongInfoDto> getSongMetadata(@PathVariable long id) throws SongInfoNotFoundException {
            log.info("GetSongMetadata invoked with params: {}", id);
            var songInfo = songInfoService.getMetadata(id);
            return ResponseEntity.ok().body(songInfo);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteSongMedatada(@RequestParam String ids) {
        log.info("DeleteSongMedatada invoked with params: {}", ids);
        var deletedIds = songInfoService.deleteSongInfos(ids);
        return ResponseEntity.ok(Map.of("ids", deletedIds));
    }
}
