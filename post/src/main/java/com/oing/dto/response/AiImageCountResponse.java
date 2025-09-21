package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 이미지 횟수 응답")
public record AiImageCountResponse(
        @Schema(description = "AI 이미지 횟수", example = "2")
        int count
) {
}
