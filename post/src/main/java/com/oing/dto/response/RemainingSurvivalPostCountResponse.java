package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미션 키 획득까지 남은 생존신고 업로드 수 응답")
public record RemainingSurvivalPostCountResponse(
        @Schema(description = "미션 키 획득까지 남은 생존신고 업로드 수", example = "3")
        Integer leftUploadCountUntilMissionUnlock
) {
}
