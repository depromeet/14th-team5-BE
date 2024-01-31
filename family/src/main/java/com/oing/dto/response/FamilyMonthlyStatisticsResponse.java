package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "월별 요약 정보")
public record FamilyMonthlyStatisticsResponse(
        @Schema(description = "전체 사진 수", example = "124")
        Integer totalImageCnt
) {
}
