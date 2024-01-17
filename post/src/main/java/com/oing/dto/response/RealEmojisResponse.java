package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "회원이 생성한 리얼 이모지 리스트 응답")
public record RealEmojisResponse(
        @Schema(description = "회원이 생성한 리얼 이모지 정보")
        List<RealEmojiResponse> myRealEmojiList
) {
}
