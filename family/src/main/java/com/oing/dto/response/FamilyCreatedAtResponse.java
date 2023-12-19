package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(description = "가족 응답")
public record FamilyCreatedAtResponse(
        @Schema(description = "가족 생성 날짜", example = "2021-12-05T12:30:00.000+09:00")
        ZonedDateTime createdAt
) {
}
