package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

public record AdminDashboardResponse(
        @Schema(description = "전체 사용자 수")
        DashboardValueResponse totalMember,

        @Schema(description = "전체 가족 수")
        DashboardValueResponse totalFamily,

        @Schema(description = "전체 게시물 수")
        DashboardValueResponse totalPost,

        @Schema(description = "전체 댓글 수")
        DashboardValueResponse totalComment,

        @Schema(description = "전체 반응 수")
        DashboardValueResponse totalReaction,

        @Schema(description = "가족별 사용자 수 분포")
        Collection<DashboardDistributionResponse> familyMemberDistribution
) {
}
