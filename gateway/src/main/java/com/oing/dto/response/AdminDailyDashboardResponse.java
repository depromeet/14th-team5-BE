package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Schema(description = "일별 대시보드 통계")
public record AdminDailyDashboardResponse(
        @Schema(description = "일별 가입자 수")
        Map<LocalDate, DashboardValueResponse> dailyMemberRegistration,

        @Schema(description = "일별 게시물 수")
        Map<LocalDate, DashboardValueResponse> dailyPostCreation
) {
}
