package com.example.resourcesongservice.client;

import com.example.resourcesongservice.dto.SongInfoDto;

public interface SongServiceClient {

    void saveMetadata(SongInfoDto songInfoDto);
}
