package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족이 올린 AI 이미지 총 개수 및 AI 이미지 생성 가능 횟수 응답")
public record AiImageCountResponse(
        @Schema(description = "가족의 AI 이미지 개수", example = "6")
        int familyAiImageCount,

        @Schema(description = "AI 이미지 생성 가능 횟수", example = "2")
        int availableAiImageCount
) {
}
