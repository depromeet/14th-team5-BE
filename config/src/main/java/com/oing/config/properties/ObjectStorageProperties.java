package com.oing.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ObjectStorageProperties {
    private final String region;
    private final String endPoint;
    private final String accessKey;
    private final String secretKey;

    public ObjectStorageProperties(@Value("${cloud.ncp.region}") String region,
                                   @Value("${cloud.ncp.end-point}") String endPoint,
                                   @Value("${cloud.ncp.credentials.access-key}") String accessKey,
                                   @Value("${cloud.ncp.credentials.secret-key}") String secretKey) {
        this.region = region;
        this.endPoint = endPoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
}
