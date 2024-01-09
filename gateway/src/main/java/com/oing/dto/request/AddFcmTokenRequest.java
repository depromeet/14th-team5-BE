package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "FCM 토큰 추가 요청")
public record AddFcmTokenRequest(
        @NotEmpty
        @Schema(description = "FCM 토큰", example = "blabla")
        String fcmToken
) {
}
