package com.oing.dto.response;

import com.oing.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족 구성원 프로필 응답")
public record FamilyMemberProfileResponse(
        @Schema(description = "구성원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E")
        String memberId,

        @Schema(description = "구성원 이름", example = "디프만")
        String name,

        @Schema(description = "구성원 프로필 이미지 주소", example = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl
) {
    public static FamilyMemberProfileResponse of(String memberId, String name, String imageUrl) {
        return new FamilyMemberProfileResponse(memberId, name, imageUrl);
    }

    public static FamilyMemberProfileResponse of(Member member) {
        return of(member.getId(), member.getName(), member.getProfileImgUrl());
    }
}
