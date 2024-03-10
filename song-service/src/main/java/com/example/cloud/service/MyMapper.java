package com.example.cloud.service;

import com.example.cloud.data.SongInfo;
import com.example.cloud.dto.SongInfoDto;
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
