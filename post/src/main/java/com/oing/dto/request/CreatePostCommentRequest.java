package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/13
 * Time: 11:30 PM
 */
@Schema(description = "피드 게시물 댓글 생성 요청")
public record CreatePostCommentRequest(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "content", example = "댓글 내용", maxLength = 255)
        String content
) {
}
