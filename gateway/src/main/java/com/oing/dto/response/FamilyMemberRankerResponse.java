package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족구성원 랭커 응답")
public record FamilyMemberRankerResponse(
        @Schema(description = "랭커의 프로필 사진 Url", example = "https://no5ing.com/profile/1.jpg")
        String profileImageUrl,

        @Schema(description = "랭커의 이름", example = "권순찬")
        String name,

        @Schema(description = "랭커의 생존 신고 횟수")
        Integer survivalCount
) {
}
