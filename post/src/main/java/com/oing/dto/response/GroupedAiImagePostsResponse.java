package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

@Schema(description = "AI 이미지 게시물 타입별 그룹화 전체 응답")
public record GroupedAiImagePostsResponse(
        @Schema(description = "AI 게시물 타입별 그룹화된 데이터 목록")
        Collection<AiPostTypeGroupResponse> aiPostGroups
) {
    public static GroupedAiImagePostsResponse of(Collection<AiPostTypeGroupResponse> groups) {
        return new GroupedAiImagePostsResponse(groups);
    }
}
