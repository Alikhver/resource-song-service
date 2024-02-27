package com.example.resourcesongservice.service;

import com.example.resourcesongservice.data.SongInfo;
import com.example.resourcesongservice.data.SongInfoRepository;
import com.example.resourcesongservice.dto.SongInfoDto;
import com.example.resourcesongservice.exception.SongInfoNotFoundException;
import com.example.resourcesongservice.exception.WrongMetadataProvidedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongInfoServiceImpl implements com.example.resourcesongservice.service.SongInfoService {

    private final SongInfoRepository repository;
    private final MyMapper mapper;

    @Override
    public Long createNewMetadata(SongInfoDto songInfoDto) {
        log.info("CreateNewMetadata invoked with param: {}", songInfoDto);

        var songInfo = mapper.toEntity(songInfoDto);
        validateSongInfo(songInfo);
        var songInfoId = repository.save(songInfo).getId();

        log.info("New SongInfo saved with params: {}", songInfoDto);
        return songInfoId;
    }

    @Override
    public SongInfoDto getMetadata(long id) throws SongInfoNotFoundException {
        log.info("CreateNewMetadata invoked with param: {}", id);

        var songInfo = repository.findById(id)
                .orElseThrow(SongInfoNotFoundException::new);
        return mapper.toDto(songInfo);
    }

    @Override
    public List<Long> deleteSongInfos(String ids) {
        log.info("deleteSongInfos invoked with param: {}", ids);
        var idsList = new ArrayList<Long>();
        for (String s : ids.split(",")) {
            try {
                idsList.add(Long.parseLong(s));
            } catch (NumberFormatException ignored) {}
        }

        var deletedIds = new ArrayList<Long>();
        idsList.forEach(id -> {
            if (repository.existsById(id)) {
                log.info("SongInfo id: {} deleted", ids);
                repository.deleteById(id);
                deletedIds.add(id);
            }
        });

        return deletedIds;
    }

    @Override
    @Transactional
    public List<Long> deleteSongInfoByResourceIds(String resourcesIds) {
        log.info("deleteSongInfoByResourceIds invoked with param: {}", resourcesIds);
        var idsList = new ArrayList<Long>();
        for (String s : resourcesIds.split(",")) {
            try {
                idsList.add(Long.parseLong(s));
            } catch (NumberFormatException ignored) {}
        }

        var deletedResourcesIds = new ArrayList<Long>();
        idsList.forEach(id -> {
            if (repository.existsByResourceId(id)) {
                repository.removeByResourceId(id);
                log.info("SongInfo by resource id: {} deleted", id);
                deletedResourcesIds.add(id);
            }
        });

        return deletedResourcesIds;
    }

    private void validateSongInfo(SongInfo songInfo) {
        //validate length
        if (!validateResourceId(songInfo.getResourceId())
                || !validateLength(songInfo.getLength())) {
            throw new WrongMetadataProvidedException();
        }
    }

    private boolean validateResourceId(long resourceId) {
        return !repository.existsByResourceId(resourceId);
    }

    private boolean validateLength(String length) {
        Pattern regexPattern = Pattern.compile("\\d{1,2}:\\d{2}");

        Matcher matcher = regexPattern.matcher(length);
        return matcher.matches();
    }
}
