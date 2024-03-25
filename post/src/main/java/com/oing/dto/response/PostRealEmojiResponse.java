package com.oing.dto.response;


import com.oing.domain.RealEmoji;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드 게시물 리얼 이모지 응답")
public record PostRealEmojiResponse(
        @Schema(description = "피드 게시물 리얼 이모지 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postRealEmojiId,

        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJUUDIF99RRS2E97")
        String postId,

        @Schema(description = "리얼 이모지 작성 사용자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "리얼 이모지 타입", example = "emoji_1")
        String emojiType,

        @Schema(description = "리얼 이모지 ID", example = "01HGW2N7EHJVEFEFEEEEES2E97")
        String realEmojiId,

        @Schema(description = "피드 게시물 리얼 이모지 이미지 주소", example = "http://test.com/test-profile.jpg")
        String emojiImageUrl
) {
    public static PostRealEmojiResponse from(RealEmoji postRealEmoji) {
        return new PostRealEmojiResponse(postRealEmoji.getId(), postRealEmoji.getPost().getId(), postRealEmoji.getMemberId(),
                postRealEmoji.getRealEmoji().getType().getTypeKey(), postRealEmoji.getRealEmoji().getId(),
                postRealEmoji.getRealEmoji().getRealEmojiImageUrl());
    }
}
