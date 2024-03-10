package com.example.cloud.service;

import com.example.cloud.dto.SongInfoDto;
import com.example.cloud.exception.SongInfoNotFoundException;

import java.util.List;

public interface SongInfoService {
    Long createNewMetadata(SongInfoDto songInfo);

    SongInfoDto getMetadata(long id) throws SongInfoNotFoundException;

    List<Long> deleteSongInfos(String ids);
}
