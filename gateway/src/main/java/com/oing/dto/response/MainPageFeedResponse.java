package com.oing.dto.response;

import java.time.ZonedDateTime;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:05 PM
 */
public record MainPageFeedResponse(
        String postId,
        String imageUrl,
        String authorName,
        ZonedDateTime createdAt
) {
}
