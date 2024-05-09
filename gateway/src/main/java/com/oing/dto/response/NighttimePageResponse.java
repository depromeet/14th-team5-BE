package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "야간 메인 페이지")
public record NighttimePageResponse(
        @Schema(description = "상단 바 요소")
        List<MainPageTopBarResponse> topBarElements,

        @Schema(description = "금월의 가족 구성원 월간 랭킹")
        FamilyMemberMonthlyRankingResponse familyMemberMonthlyRanking
) {
}
