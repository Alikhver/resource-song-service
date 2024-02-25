package com.example.resourcesongservice.web;

import com.example.resourcesongservice.dto.SongInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/songs")
public class SongController {

    @PostMapping
    public void createNewMetadata(@RequestBody SongInfoDto songInfo) {
        log.info("CreateNewMetadata invoked with params: {}", songInfo);

    }

    @GetMapping("/{id}")
    public SongInfoDto getSongMetadata(@PathVariable int id) {
        log.info("GetSongMetadata invoked with params: {}", id);
        return null;
    }

    @DeleteMapping
    public void deleteSongMedatada(@RequestParam String ids) {
        log.info("DeleteSongMedatada invoked with params: {}", ids);
    }
}
