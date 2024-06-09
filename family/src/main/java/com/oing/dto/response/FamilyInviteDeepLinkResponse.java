package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "가족 초대 링크 응답")
public record FamilyInviteDeepLinkResponse(

        @Schema(description = "가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String familyId,

        @Schema(description = "가족 명", example = "사랑하는 우리가족")
        String familyName,

        @Schema(description = "대표 가족구성원 닉네임", example = "미밍이")
        List<String> familyMemberNames,

        @Schema(description = "대표 가족구성원 프로필 이미지 URL", example = "https://asset.no5ing.kr/member/profile/ab3fde93ee")
        List<String> familyMembersProfileImageUrls,

        @Schema(description = "대표 가족구성원 외 구성원 수", example = "3")
        Integer extraFamilyMembersCount,

        @Schema(description = "전체 가족 구성원 수", example = "5")
        Integer familyMembersCount,


        @Schema(description = "초대자 이름", example = "김철수")
        String inviterName,

        @Schema(description = "완료한 생존신고의 수", example = "3")
        Integer survivalCount,

        @Schema(description = "요청자가 이미 가족에 가입되어 있는지 여부 (토큰 첨부 시에만 반환, 미첨부 시 null 반환)", example = "true")
        Boolean isRequesterJoinedFamily
) {
}
