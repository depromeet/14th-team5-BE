package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateAiImageRequest(
        @NotNull
        @Schema(description = "피드 게시물 사진 주소", example = "https://no5ing.com/feed/1.jpg")
        String imageUrl
) {
}
