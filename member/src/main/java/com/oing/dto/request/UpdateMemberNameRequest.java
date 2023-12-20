package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원 이름 수정 요청")
public record UpdateMemberNameRequest(
        @NotBlank
        @Size(min = 2, max = 10)
        @Schema(description = "회원 이름", example = "홍길동")
        String name
) {
}
