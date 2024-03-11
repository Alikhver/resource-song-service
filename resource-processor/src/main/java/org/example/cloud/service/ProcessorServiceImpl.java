package org.example.cloud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.example.cloud.client.ResourceServiceClient;
import org.example.cloud.client.SongServiceClient;
import org.example.cloud.dto.SongInfoDto;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessorServiceImpl implements ProcessorService {
    private final ResourceServiceClient resourceClient;
    private final SongServiceClient songClient;

    @Override
    public void parseAndCreateMetadata(Long resourceId) throws TikaException, IOException, SAXException {
        byte[] content = resourceClient.getResource(resourceId);

        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext pContext = new ParseContext();

        Mp3Parser Mp3Parser = new Mp3Parser();
        Mp3Parser.parse(new ByteArrayInputStream(content), handler, metadata, pContext);

        //TODO add validation
        SongInfoDto songInfoDto = SongInfoDto.builder()
                .name(parseName(metadata))
                .artist(parseArtist(metadata))
                .album(parseAlbum(metadata))
                .length(parseLength(metadata))
                .year(parseYear(metadata))
                .resourceId(resourceId)
                .build();

        songClient.saveMetadata(songInfoDto);
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
