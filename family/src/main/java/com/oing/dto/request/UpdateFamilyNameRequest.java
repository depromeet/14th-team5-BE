package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "가족 이름 수정 요청")
public record UpdateFamilyNameRequest(

        @Size(max = 9)
        @Schema(description = "가족 이름", example = "오잉")
        String familyName
) {
}
