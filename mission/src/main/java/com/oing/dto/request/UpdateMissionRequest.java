package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "미션 수정 요청")
public record UpdateMissionRequest(

        @NotBlank
        @Schema(description = "미션 내용", example = "오늘의 기분을 나타내는 사진 찍기.")
        String content
) {
}
