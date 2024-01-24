package com.oing.dto.response;

import com.oing.domain.BannerImageType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배너 응답")
public record BannerResponse(
        @Schema(description = "가족 활성도의 상위 백분율", example = "50")
        Integer familyTopPercentage,

        @Schema(description = "가족 구성원 모두가 업로드한 날의 수", example = "3")
        Integer allFamilyMembersUploadedDays,

        @Schema(description = "가족 활성도 레벨 (1 ~ 4)", example = "1")
        Integer familyLevel,

        @Schema(description = "배너 이미지 타입", example = "SKULL_FLAG")
        BannerImageType bannerImageType
) {
}
