package org.example.cloud.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloud.config.SongClientProperties;
import org.example.cloud.dto.SongInfoDto;
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
        ServiceInstance instance = loadBalancerClient.choose(songClientProperties.getId());
        log.info("Song Service instance: {} {} chosen for create request with param: {} ", instance.getInstanceId(), instance.getServiceId(), songInfoDto);

        log.info("Sending create request to Song Service: {}", songInfoDto);
        webClient.post()
                .uri(uri -> uri.scheme(instance.getScheme())
                        .host(instance.getHost())
                        .port(instance.getPort())
                        .path(songClientProperties.getEndpoint())
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(songInfoDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
