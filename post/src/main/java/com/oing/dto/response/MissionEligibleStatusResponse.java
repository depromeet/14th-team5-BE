package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 미션 참여 가능 여부 응답")
public record MissionEligibleStatusResponse(
        @Schema(description = "회원 미션 참여 가능 여부", example = "true")
        boolean isValid
) {
}
