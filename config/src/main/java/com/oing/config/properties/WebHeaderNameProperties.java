package com.oing.config.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/21
 * Time: 5:13 PM
 */
@ConfigurationPropertiesBinding
public record WebHeaderNameProperties(
        String accessToken
) {
    public WebHeaderNameProperties {
        if(accessToken == null) throw new IllegalArgumentException("accessToken cannot be null");
    }

}
