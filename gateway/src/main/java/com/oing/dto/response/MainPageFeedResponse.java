package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:05 PM
 */
@Schema(description = "메인 페이지 피드 요소")
public record MainPageFeedResponse(
        @Schema(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "게시물 이미지 URL", example = "https://no5ing.com/image/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl,

        @Schema(description = "게시물 작성자 이름", example = "엄마")
        String authorName,

        @Schema(description = "게시물 생성 일자", example = "2024-04-16T16:05:00+09:00")
        ZonedDateTime createdAt
) {
}
