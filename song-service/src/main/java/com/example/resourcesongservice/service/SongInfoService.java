package com.example.resourcesongservice.service;

import com.example.resourcesongservice.dto.SongInfoDto;
import com.example.resourcesongservice.exception.SongInfoNotFoundException;

import java.util.List;

public interface SongInfoService {
    Long createNewMetadata(SongInfoDto songInfo);

    SongInfoDto getMetadata(long id) throws SongInfoNotFoundException;

    List<Long> deleteSongInfos(String ids);

    List<Long> deleteSongInfoByResourceIds(String ids);

}
