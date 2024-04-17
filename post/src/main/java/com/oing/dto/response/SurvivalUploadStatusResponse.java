package com.oing.dto.response;

import com.oing.domain.Reaction;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 생존신고 게시글 업로드 여부 응답")
public record SurvivalUploadStatusResponse(
        @Schema(description = "회원 생존신고 게시글 업로드 여부", example = "true")
        boolean isValid
) {
}
