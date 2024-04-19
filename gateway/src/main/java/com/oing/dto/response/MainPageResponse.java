package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "메인 페이지")
public record MainPageResponse(
        @Schema(description = "상단 바 요소")
        List<MainPageTopBarResponse> topBarElements,

        @Schema(description = "미션 해금 여부", example = "true")
        boolean isMissionUnlocked,

        @Schema(description = "생존 피드 목록")
        List<MainPageFeedResponse> survivalFeeds,

        @Schema(description = "미션 피드 목록")
        List<MainPageFeedResponse> missionFeeds,

        @Schema(description = "날 찌른 목록")
        List<MainPagePickerResponse> pickers
) {
}
