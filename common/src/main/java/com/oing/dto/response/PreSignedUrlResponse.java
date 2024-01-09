package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "PreSingedUrl 요청 후 응답")
public record PreSignedUrlResponse(
        @NotNull
        @Schema(description = "PreSingedUrl", example = "https://asset.no5ing.kr/post/01HGW2N7EHEEE7")
        String url
) {
}
