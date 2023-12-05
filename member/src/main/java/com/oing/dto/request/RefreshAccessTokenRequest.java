package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 11:13 AM
 */
@Schema(description = "기존 토큰 리프레시 요청")
public record RefreshAccessTokenRequest(
        @NotBlank @Schema(description = "기존에 발행된 리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIdI6MjEwMCwiaWF0IjoxNjM4NjM2NDI3fQ.5zBBo9LMi9Y_L6gyN0WYq41Qn2GJGSySMs7XJ6c_aFk") String refreshToken
) {
}
