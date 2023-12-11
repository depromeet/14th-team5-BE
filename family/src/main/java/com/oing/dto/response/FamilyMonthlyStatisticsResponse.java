package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "월별 요약 정보")
public record FamilyMonthlyStatisticsResponse(
        @Schema(description = "모두 참여한 날", example = "12")
        Integer totalParticipateCnt,

        @Schema(description = "전체 사진 수", example = "124")
        Integer totalImageCnt,

        @Schema(description = "나의 사진 수", example = "38")
        Integer myImageCnt
) {
}
