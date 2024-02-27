package com.example.resourcesongservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("my.client.bravo")
public class BravoClientProperties {
    private String scheme;
    private String host;
    private String port;
    private String endpoint;
}
