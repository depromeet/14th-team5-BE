package com.oing.dto.response;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "가족구성원 월간 랭킹 응답")
public record FamilyMemberMonthlyRankingResponse(

        @Schema(description = "랭킹 기준 월")
        Integer month,

        @Schema(description = "1등 랭커 Dto")
        FamilyMemberRankerResponse firstRanker,

        @Schema(description = "2등 랭커 Dto")
        FamilyMemberRankerResponse secondRanker,

        @Schema(description = "3등 랭커 Dto")
        FamilyMemberRankerResponse thirdRanker,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(description = "가장 최근에 생존신고한 날", example = "2023-12-05")
        LocalDate mostRecentSurvivalPostDate
) {
}
