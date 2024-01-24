package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배너 응답")
public record BannerResponse(
        @Schema(description = "가족 활성도의 상위 백분율", example = "50")
        Integer familyTopPercentage,

        @Schema(description = "가족 구성원 모두가 업로드한 날의 수", example = "3")
        Integer allFamilyMembersUploadedDays
) {
}
