package com.oing.dto.response;

import com.oing.domain.Reaction;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드 게시물 응답")
public record PostReactionResponse(
        @Schema(description = "피드 게시물 반응 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String reactionId,

        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "반응 작성 사용자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "피드 게시물 반응 타입", example = "EMOJI_1")
        String emojiType
) {
    public static PostReactionResponse from(Reaction postReaction) {
        return new PostReactionResponse(
                postReaction.getId(),
                postReaction.getPost().getId(),
                postReaction.getMemberId(),
                postReaction.getEmoji().getTypeKey()
        );
    }
}
