package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족 구성원 프로필 및 가족 생성일 응답")
public record FamilyMemberProfilesResponse(
        @Schema(description = "가족 구성원 프로필 정보")
        PaginationResponse<FamilyMemberProfileResponse> contents,

        @Schema(description = "가족 생성일", example = "2023-12-19")
        String createdAt
) {
}
