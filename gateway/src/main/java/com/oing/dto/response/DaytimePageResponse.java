package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주간 메인 페이지")
public record DaytimePageResponse(
        @Schema(description = "상단 바 요소")
        List<MainPageTopBarResponse> topBarElements,

        @Schema(description = "날 찌른 목록")
        List<MainPagePickerResponse> pickers,

        @Schema(description = "미션 해금까지 남은 업로드 횟수", example = "3")
        Integer leftUploadCountUntilMissionUnlock,

        @Schema(description = "미션 해금 여부", example = "true")
        boolean isMissionUnlocked,

        @Schema(description = "오늘 요청자가 생존신고를 업로드 했는지 여부", example = "true")
        boolean isMeSurvivalUploadedToday,

        @Schema(description = "오늘 요청자가 미션을 업로드 했는지 여부", example = "true")
        boolean isMeMissionUploadedToday,

        @Schema(description = "오늘의 미션 내용 (모킹됨)", example = "오늘의 기분을 나타내는 사진 찍기.")
        String dailyMissionContent,

        @Schema(description = "생존 피드 목록")
        List<MainPageFeedResponse> survivalFeeds,

        @Schema(description = "미션 피드 목록")
        List<MainPageFeedResponse> missionFeeds
) {
}
