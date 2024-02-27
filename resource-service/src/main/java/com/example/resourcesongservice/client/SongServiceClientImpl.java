package com.example.resourcesongservice.client;

import com.example.resourcesongservice.dto.SongInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class SongServiceClientImpl implements SongServiceClient {

    private final WebClient webClient;

    @Override
    public void saveMetadata(SongInfoDto songInfoDto) {
        log.info("Sending create request to Song Service: {}", songInfoDto);
        webClient.post()
                .uri("http://localhost:7083/songs")
//                .uri(uri -> uri.scheme(bravoClientConfig.getScheme())
//                        .host(bravoClientConfig.getHost())
//                        .port(bravoClientConfig.getPort())
//                        .path(bravoClientConfig.getEndpoint())
//                        .build())
                .bodyValue(songInfoDto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().toBodilessEntity().block();
    }

    @Override
    public void deleteMetadataByResourceId(Long id) {
        log.info("Sending delete by resource id request to Song Service: {}", id);
        webClient.delete()
                .uri("http://localhost:7083/songs/by-resource?resourceIds=" + id)
//                .uri(uri -> uri.scheme(bravoClientConfig.getScheme())
//                        .host(bravoClientConfig.getHost())
//                        .port(bravoClientConfig.getPort())
//                        .path(bravoClientConfig.getEndpoint())
//                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().toBodilessEntity().block();
    }
}
