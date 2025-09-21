package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 이미지 생성 응답")
public record AiImageResponse(
        @Schema(description = "AI 이미지 주소", example = "https://no5ing.com/ai/chusoek/1.jpg")
        String AiImageUrl
) {
}
