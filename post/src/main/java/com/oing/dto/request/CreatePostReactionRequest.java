package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/11
 * Time: 11:30 PM
 */
@Schema(description = "피드 게시물 반응 생성 요청")
public record CreatePostReactionRequest(
        @NotBlank
        @Schema(description = "이모지", example = "smile",
                allowableValues = {"smile", "heart", "clap", "thumbs-up", "laugh"})
        String content
) {
}
