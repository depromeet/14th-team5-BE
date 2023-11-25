package com.oing.config.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:14 PM
 */
@ConfigurationPropertiesBinding
public record TokenExpirationProperties(
        String accessToken,
        String refreshToken
) {
    public TokenExpirationProperties {
        if(accessToken == null) throw new IllegalArgumentException("accessToken cannot be null");
        if(refreshToken == null) throw new IllegalArgumentException("refreshToken cannot be null");
    }

}
