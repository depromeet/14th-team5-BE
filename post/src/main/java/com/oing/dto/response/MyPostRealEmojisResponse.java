package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "자신이 생성한 리얼 이모지 리스트 응답")
public record MyPostRealEmojisResponse(
        @Schema(description = "자신이 생성한 리얼 이모지 정보")
        Map<String, String> myRealEmojiList
) {
}
