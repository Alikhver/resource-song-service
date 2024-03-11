package org.example.cloud.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloud.config.ResourceClientProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceServiceClientImpl implements ResourceServiceClient {
    private final WebClient webClient;
    private final ResourceClientProperties resourceClientProperties;
    private final LoadBalancerClient loadBalancer;

    @Override
    public byte[] getResource(Long id) {
        log.info("Sending get request to resource service: {}", id);

        ServiceInstance instance = loadBalancer.choose(resourceClientProperties.getId());
//        try {
        byte[] content = webClient.get()
                .uri(uri -> uri.scheme(instance.getScheme())
                        .host(instance.getHost())
                        .port(instance.getPort())
                        .path(resourceClientProperties.getEndpoint())
                        .build(id))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(byte[].class)
//                    .bodyToMono(String.class) // Получаем ответ в виде строки
//                    .map(String::getBytes)
                .block();
            log.info("Response from Resource service received. Length: {}", content.length);
//            return content.getBytes();
        return content;
//        } catch (WebClientResponseException | NullPointerException e) {
//
//        }
    }
}
