package com.oing.dto.response;

import com.oing.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Schema(description = "피드 게시물 댓글 응답")
public record PostCommentResponse(
        @Schema(description = "피드 게시물 댓글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String commentId,

        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "댓글 작성 사용자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "피드 게시물 내용", example = "정말 환상적인 하루였네요!")
        String comment,

        @Schema(description = "댓글 작성 시간", example = "2023-12-23T01:53:21.577347+09:00")
        ZonedDateTime createdAt
) {
    public static PostCommentResponse from(Comment postComment) {
        return new PostCommentResponse(
                postComment.getId(),
                postComment.getPost().getId(),
                postComment.getMemberId(),
                postComment.getContent(),
                postComment.getCreatedAt() != null ? postComment.getCreatedAt().atZone(ZoneId.systemDefault()) : null
        );
    }
}
