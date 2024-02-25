package com.example.resourcesongservice.dto;

import lombok.Data;

@Data
public class SongInfoDto {
    private String name;
    private String artist;
    private String album;
    private String length;
    private int resourceId;
    private int year;
}
