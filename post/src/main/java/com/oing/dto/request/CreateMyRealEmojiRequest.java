package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "자신의 리얼 이모지 생성 요청")
public record CreateMyRealEmojiRequest(

        @Schema(description = "리얼 이모지 타입", example = "EMOJI_1")
        String type,

        @NotNull
        @Schema(description = "리얼 이모지 사진 주소", example = "https://no5ing.com/feed/1.jpg")
        String imageUrl
) {
}
