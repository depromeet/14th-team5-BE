package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "피드 게시물 음성 댓글 생성 요청")
public record CreatePostVoiceCommentRequest(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "fileUrl", example = "음성 댓글 파일 주소", maxLength = 255)
        String fileUrl
) {
}
