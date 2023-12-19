package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/11
 * Time: 11:30 PM
 */
@Schema(description = "피드 게시물 생성 요청")
public record CreatePostRequest(
        @NotNull
        @Schema(description = "피드 게시물 사진 주소", example = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl,

        @Schema(description = "피드 게시물 내용", example = "안녕하세요")
        String content,

        @NotNull
        @Schema(description = "피드 게시물 작성 시간", example = "2021-12-05T12:30:00.000+09:00")
        ZonedDateTime uploadTime
) {
}
