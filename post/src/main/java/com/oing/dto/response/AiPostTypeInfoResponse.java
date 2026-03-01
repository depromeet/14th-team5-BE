package com.oing.dto.response;

import com.oing.domain.AiPostType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "AI 게시물 타입 정보 응답")
public record AiPostTypeInfoResponse(
        @Schema(description = "AI 게시물 타입", example = "CHRISTMAS_2025")
        String aiPostType,

        @Schema(description = "AI 게시물 타입 이미지 URL")
        String imageUrl,

        @Schema(description = "시작일", example = "2025-12-23")
        LocalDate startDate,

        @Schema(description = "종료일", example = "2025-12-31")
        LocalDate endDate,

        @Schema(description = "가족의 해당 타입 게시물 수", example = "3")
        int postCount
) {
    public static AiPostTypeInfoResponse of(AiPostType aiPostType, String imageUrl, int postCount) {
        return new AiPostTypeInfoResponse(
                aiPostType.getTypeKey(),
                imageUrl,
                aiPostType.getStartDate(),
                aiPostType.getEndDate(),
                postCount
        );
    }
}
