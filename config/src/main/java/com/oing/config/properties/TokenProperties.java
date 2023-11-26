package com.oing.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.regex.Pattern;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:15 PM
 */
@ConfigurationProperties(prefix = "app.token")
@ConfigurationPropertiesBinding
public record TokenProperties(
        String secretKey,
        @NestedConfigurationProperty TokenExpirationProperties expiration
) {

}
