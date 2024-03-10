package com.example.resourcesongservice.service;

import com.example.resourcesongservice.client.SongServiceClient;
import com.example.resourcesongservice.client.StorageClient;
import com.example.resourcesongservice.data.Resource;
import com.example.resourcesongservice.data.ResourceRepository;
import com.example.resourcesongservice.dto.SongInfoDto;
import com.example.resourcesongservice.exception.ResourceNotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final SongServiceClient songClient;
    private final ResourceRepository resourceRepository;
    private final StorageClient storageClient;

    @Override
    public byte[] getResourceById(Long id) {
        log.info("GetResourceById invoked with param: {}", id);

        String s3contentKey = resourceRepository.findById(id)
                .orElseThrow(ResourceNotFoundByIdException::new)
                .getS3ContentKey();

        return storageClient.download(s3contentKey);
    }

    @Override
    public List<Long> deleteByIds(String idsString) {
        var resourcesToDelete = new ArrayList<Resource>();
        for (String s : idsString.split(",")) {
            try {
                var resource = resourceRepository.findById(Long.parseLong(s))
                        .orElseThrow(ResourceNotFoundByIdException::new);
                resourcesToDelete.add(resource);
            } catch (NumberFormatException | ResourceNotFoundByIdException ignored) {
            }
        }

        var deletedIds = new ArrayList<Long>();
        resourcesToDelete.forEach(resource -> {
                Long resourceId = resource.getId();

                //TODO add rollback conditions when other request were not successful and vice versa
                songClient.deleteMetadataByResourceId(resourceId);
                storageClient.delete(resource.getS3ContentKey());
                resourceRepository.deleteById(resourceId);
                log.info("Resource with id = {} deleted", resourceId);
                //TODO split metadata deletion and resource deletion. Call deleteMetadataByResourceId once with all ids

                deletedIds.add(resourceId);
        });

        return deletedIds;
    }

    @Override
    public Long createResource(InputStream data) throws TikaException, IOException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext pContext = new ParseContext();
        byte[] byteArray = data.readAllBytes();

        Mp3Parser Mp3Parser = new Mp3Parser();
        Mp3Parser.parse(new ByteArrayInputStream(byteArray), handler, metadata, pContext);

        String s3ContentKey = uploadContentToS3(byteArray);

        Resource resource = new Resource();
        resource.setS3ContentKey(s3ContentKey);

        var id = resourceRepository.save(resource).getId();
        log.info("New Resource saved: {}", resource);

        //TODO add validation
        SongInfoDto songInfoDto = SongInfoDto.builder()
                .name(parseName(metadata))
                .artist(parseArtist(metadata))
                .album(parseAlbum(metadata))
                .length(parseLength(metadata))
                .year(parseYear(metadata))
                .resourceId(id)
                .build();

        songClient.saveMetadata(songInfoDto);
        return id;
    }

    private String uploadContentToS3(byte[] content) {
        String s3ContentKey = UUID.randomUUID().toString();
        storageClient.upload(s3ContentKey, content);
        return s3ContentKey;
    }

    private String parseYear(Metadata metadata) {
        return metadata.get("xmpDM:releaseDate");
    }

    private String parseName(Metadata metadata) {
        return metadata.get("dc:title");
    }

    private String parseAlbum(Metadata metadata) {
        return metadata.get("xmpDM:album");
    }

    private String parseArtist(Metadata metadata) {
        return metadata.get("xmpDM:albumArtist");
    }

    private String parseLength(Metadata metadata) {
        String durationString = metadata.get("xmpDM:duration");
        double milliseconds = Double.parseDouble(durationString);
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
