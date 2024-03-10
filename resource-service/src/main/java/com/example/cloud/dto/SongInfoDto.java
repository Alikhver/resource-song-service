package com.example.cloud.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongInfoDto {
    private String name;
    private String artist;
    private String album;
    private String length;
    private long resourceId;
    private String year;
}
