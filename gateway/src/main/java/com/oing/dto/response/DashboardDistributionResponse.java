package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대시보드 분포 응답")
public record DashboardDistributionResponse(
        @Schema(description = "실제 개수", example = "4")
        Integer count,

        @Schema(description = "차지하는 비율 (단위: 퍼센트)", example = "40.5")
        Double percent
) {

}
