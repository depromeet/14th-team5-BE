package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족 초대 링크 응답")
public record FamilyInvitationLinkResponse(
        @Schema(description = "가족 초대 링크", example = "https://no5ing.kr/o/be9238cSDfke")
        String url
) {
}
