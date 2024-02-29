package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대시보드 수치 값")
public record DashboardValueResponse(
        @Schema(description = "해당 값의 전체 수", example = "5096")
        Integer count,

        @Schema(description = "어제와 오늘의 추이 (단위: 퍼센트)", example = "20.3")
        Double percentBetweenYesterday
) {
}
