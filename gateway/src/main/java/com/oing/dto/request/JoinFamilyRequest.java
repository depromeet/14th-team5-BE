package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "가족 가입 요청")
public record JoinFamilyRequest(
        @NotEmpty
        @Schema(description = "딥링크 코드", example = "blabla")
        String inviteCode
) {
}
