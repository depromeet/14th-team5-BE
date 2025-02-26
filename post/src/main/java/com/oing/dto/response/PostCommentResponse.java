package com.oing.dto.response;

import com.oing.domain.CommentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(description = "피드 게시물 댓글 응답")
public record PostCommentResponse(
        @Schema(description = "피드 게시물 댓글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String commentId,

        @Schema(description = "타입", example = "VOICE")
        CommentType type,

        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "댓글 작성 사용자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "피드 게시물 내용", example = "정말 환상적인 하루였네요!")
        String comment,

        @Schema(description = "음성 파일 URL", example = "https://asset.no5ing.kr/post/01HGW2N7EHEEE7")
        String voiceUrl,

        @Schema(description = "댓글 작성 시간", example = "2023-12-23T01:53:21.577347+09:00")
        ZonedDateTime createdAt
) {

}
