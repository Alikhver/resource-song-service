package com.example.resourcesongservice.service;

import com.example.resourcesongservice.client.SongServiceClient;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final SongServiceClient songClient;
    private final ResourceRepository resourceRepository;

    @Override
    public byte[] getResourceById(Long id) {
        log.info("GetResourceById invoked with param: {}", id);
        return resourceRepository.findById(id)
                .orElseThrow(ResourceNotFoundByIdException::new)
                .getContent();
    }

    @Override
    public List<Long> deleteByIds(String idsString) {
        var idsList = new ArrayList<Long>();
        for (String s : idsString.split(",")) {
            try {
                idsList.add(Long.parseLong(s));
            } catch (NumberFormatException ignored) {
            }
        }

        var deletedIds = new ArrayList<Long>();
        idsList.forEach(id -> {
            if (resourceRepository.existsById(id)) {
                resourceRepository.deleteById(id);
                deletedIds.add(id);
            }
        });

        return deletedIds;
    }

    @Override
    public Long createResource(InputStream data) throws TikaException, IOException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();
        byte[] byteArray = data.readAllBytes();

        Mp3Parser Mp3Parser = new Mp3Parser();
        Mp3Parser.parse(new ByteArrayInputStream(byteArray), handler, metadata, pcontext);

        Resource resource = new Resource();
        resource.setContent(byteArray);
        long resourceId = resourceRepository.save(resource).getId();
        SongInfoDto songInfoDto = SongInfoDto.builder()
                .name(parseName(metadata))
                .artist(parseArtist(metadata))
                .album(parseAlbum(metadata))
                .length(parseLength(metadata))
                .year(parseYear(metadata))
                .resourceId(resourceId)
                .build();
        songClient.saveMetadata(songInfoDto);
        return resourceId;
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
