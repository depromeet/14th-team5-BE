package com.oing.dto.response;

import com.oing.domain.MemberRealEmoji;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원이 생성한 리얼 이모지 응답")
public record RealEmojiResponse (
        @Schema(description = "리얼 이모지 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String realEmojiId,

        @Schema(description = "리얼 이모지 타입", example = "EMOJI_1")
        String type,

        @Schema(description = "리얼 이모지 이미지 주소", example = "https://no5ing.com/profile/1.jpg")
        String imageUrl
){
    public static RealEmojiResponse from(MemberRealEmoji realEmoji) {
        return new RealEmojiResponse(realEmoji.getId(), realEmoji.getType().getTypeKey(),
                realEmoji.getRealEmojiImageUrl());
    }
}
