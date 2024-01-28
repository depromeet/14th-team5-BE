package com.oing.dto.response;

import com.oing.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/7/23
 * Time: 6:03 PM
 */
public record MemberResponse(
        @Schema(description = "구성원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E")
        String memberId,

        @Schema(description = "구성원 이름", example = "디프만")
        String name,

        @Schema(description = "구성원 프로필 이미지 주소", example = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl,

        @Schema(description = "구성원 가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E")
        String familyId,

        @Schema(description = "가족 가입 날짜", example = "2023-12-23")
        LocalDate familyJoinAt,

        @Schema(description = "구성원 생일", example = "2023-12-23")
        LocalDate dayOfBirth
) {
    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getProfileImgUrl(),
                member.getFamilyId(),
                member.getFamilyJoinAt().toLocalDate(),
                member.getDayOfBirth()
        );
    }
}
