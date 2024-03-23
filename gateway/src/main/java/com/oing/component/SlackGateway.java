package com.oing.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.config.properties.ExternalUrlProperties;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 1:00 PM
 */
@RequiredArgsConstructor
@Component
public class SlackGateway {
    private final ExternalUrlProperties externalUrlProperties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendErrorSlack(SlackBotDto dto) {
        String body = objectMapper.writeValueAsString(dto);
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> requestEntity = RequestEntity
                .post(externalUrlProperties.slackErrorWebhook())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        restTemplate.exchange(requestEntity, Void.class);
    }

    @SneakyThrows
    public void sendNotificationSlack(SlackBotDto dto) {
        String body = objectMapper.writeValueAsString(dto);
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> requestEntity = RequestEntity
                .post(externalUrlProperties.slackNotificationWebhook())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        restTemplate.exchange(requestEntity, Void.class);
    }

    @Builder
    public static class SlackBotDto {
        @JsonProperty("attachments")
        final List<SlackBotAttachmentDto> attachments;
    }

    @Builder
    public static class SlackBotAttachmentDto {
        @JsonProperty("mrkdwn_in")
        final String mrkdwn_in = "[\"text\"]";
        @JsonProperty("color")
        String color = "#ff2400";
        @JsonProperty("author_name")
        final String authorName;
        @JsonProperty("title")
        final String title;
        @JsonProperty("text")
        final String text;
        @JsonProperty("fields")
        final List<SlackBotFieldDto> fields;
    }

    @Builder
    public static class SlackBotFieldDto {
        @JsonProperty("title")
        final String title;
        @JsonProperty("value")
        final String value;
        @JsonProperty("short")
        final boolean shortField;
    }
}
