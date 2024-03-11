package org.example.cloud.client;


import org.example.cloud.dto.SongInfoDto;

public interface SongServiceClient {

    void saveMetadata(SongInfoDto songInfoDto);
}
