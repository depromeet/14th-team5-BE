package com.oing.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 1:08 PM
 */
@ConfigurationProperties(prefix = "app.external-urls")
@ConfigurationPropertiesBinding
public record ExternalUrlProperties(
        String slackWebhook
) {
}
