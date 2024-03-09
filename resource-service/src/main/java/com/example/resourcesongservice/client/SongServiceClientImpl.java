package com.example.resourcesongservice.client;

import com.example.resourcesongservice.config.SongClientProperties;
import com.example.resourcesongservice.dto.SongInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class SongServiceClientImpl implements SongServiceClient {

    private final WebClient webClient;
    private final SongClientProperties songClientProperties;
    private final LoadBalancerClient loadBalancerClient;

    @Override
    public void saveMetadata(SongInfoDto songInfoDto) {
        log.info("Sending create request to Song Service: {}", songInfoDto);

        ServiceInstance instance = loadBalancerClient.choose(songClientProperties.getId());

        log.info("Song Service instance: {} {} chosen for create request with param: {} ", instance.getInstanceId(), instance.getServiceId(), songInfoDto);
        webClient.post()
                .uri(uri -> uri.scheme(instance.getScheme())
                        .host(instance.getHost())
                        .port(instance.getPort())
                        .path(songClientProperties.getEndpoint())
                        .build())
                .bodyValue(songInfoDto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().toBodilessEntity().block();
    }

    @Override
    public void deleteMetadataByResourceId(Long id) {
        log.info("Sending delete by resource id request to Song Service: {}", id);

        ServiceInstance instance = loadBalancerClient.choose(songClientProperties.getId());
        log.info("Song Service instance: {} {} chosen for delete request with param: {} ", instance.getInstanceId(), instance.getServiceId(), id);

        webClient.delete()
                .uri(uri -> uri.scheme(instance.getScheme())
                        .host(instance.getHost())
                        .port(instance.getPort())
                        .path(songClientProperties.getEndpoint())
                        .queryParam("ids", id.toString())
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().toBodilessEntity().block();
    }
}
