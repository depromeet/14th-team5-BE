package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "피드 게시물 이모지 응답")
public record PostRealEmojiMemberResponse(
        @Schema(description = "이모지를 누른 사용자 ID 목록")
        Map<String, List<String>> emojiMemberIdsList
) {
}
