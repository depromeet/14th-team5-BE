package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "자신의 리얼 이모지 수정 요청")
public record UpdateMyRealEmojiRequest(

        @NotNull
        @Schema(description = "리얼 이모지 사진 주소", example = "https://no5ing.com/feed/1.jpg")
        String imageUrl
) {
}
