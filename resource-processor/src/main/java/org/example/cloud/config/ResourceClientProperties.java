package org.example.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("my.client.resource-service")
public class ResourceClientProperties {
    private String id;
    private String endpoint;
}
