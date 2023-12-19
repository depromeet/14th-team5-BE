package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/11
 * Time: 11:30 PM
 */
@Schema(description = "피드 게시물 생성 요청")
public record CreatePostRequest(
        @NotNull
        @Schema(description = "피드 게시물 사진 주소", example = "images/feed/01HJ1SEEEFDSA5ZZVRW.jpg")
        String imageUrl,

        @Schema(description = "피드 게시물 내용", example = "안녕하세요")
        String content
) {
}
