package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족 미션 참여 가능 (미션 키) 여부 응답")
public record MissionAvailableStatusResponse(
        @Schema(description = "가족 미션 참여 가능 (미션 키) 여부", example = "true")
        boolean isMissionUnlocked
) {
}
