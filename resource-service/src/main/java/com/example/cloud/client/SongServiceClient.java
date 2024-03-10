package com.example.cloud.client;

import com.example.cloud.dto.SongInfoDto;

public interface SongServiceClient {

    void saveMetadata(SongInfoDto songInfoDto);

    void deleteMetadataByResourceId(Long id);
}
