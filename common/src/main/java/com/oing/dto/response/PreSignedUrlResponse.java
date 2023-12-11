package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "PreSingedUrl 요청 후 응답")
public record PreSignedUrlResponse(
        @Schema(description = "PreSingedUrl", example = "")
        String url
){
        public static PreSignedUrlResponse of(String url) {
                return new PreSignedUrlResponse(url);
        }
}
