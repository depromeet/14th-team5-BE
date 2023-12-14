package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 탈퇴 요청")
public record DeleteMemberRequest(
        @Schema(description = "회원 탈퇴 사유", example = "서비스 불만족")
        String withdrawalReason
) {
}
