package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

@Schema(description = "AI 게시물 타입별 그룹화 응답")
public record AiPostTypeGroupResponse(
        @Schema(description = "AI 게시물 타입", example = "CHRISTMAS_2025")
        String aiPostType,

        @Schema(description = "해당 타입의 게시물 목록")
        Collection<PostResponse> posts
) {
    public static AiPostTypeGroupResponse of(String aiPostType, Collection<PostResponse> posts) {
        return new AiPostTypeGroupResponse(aiPostType, posts);
    }
}
