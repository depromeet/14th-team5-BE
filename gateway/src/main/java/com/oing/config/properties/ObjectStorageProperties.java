package com.oing.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties(prefix = "cloud.ncp")
@ConfigurationPropertiesBinding
public record ObjectStorageProperties(
        String region,
        String endPoint,
        String accessKey,
        String secretKey
) {
}
