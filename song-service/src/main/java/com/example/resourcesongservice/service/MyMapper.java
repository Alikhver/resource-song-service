package com.example.resourcesongservice.service;

import com.example.resourcesongservice.data.SongInfo;
import com.example.resourcesongservice.dto.SongInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MyMapper {
    SongInfo toEntity(SongInfoDto songInfoDto);
    SongInfoDto toDto(SongInfo songInfo);
}
