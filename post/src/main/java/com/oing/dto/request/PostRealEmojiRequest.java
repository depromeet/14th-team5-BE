package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "피드 게시물 리얼 이모지 생성 및 삭제 요청")
public record PostRealEmojiRequest(
        @NotBlank
        @Schema(description = "이모지 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String realEmojiId
) {
}
