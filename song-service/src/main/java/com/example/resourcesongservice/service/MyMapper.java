package com.example.resourcesongservice.service;

import com.example.resourcesongservice.data.SongInfo;
import com.example.resourcesongservice.dto.SongInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MyMapper {
    @Mapping(source = "resourceId", target = "id")
    SongInfo toEntity(SongInfoDto songInfoDto);
    @Mapping(source = "id", target = "resourceId")
    SongInfoDto toDto(SongInfo songInfo);
}
