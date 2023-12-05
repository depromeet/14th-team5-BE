package com.oing.dto.response;

import com.oing.domain.TokenPair;
import com.oing.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 후 토큰 응답")
public record AuthResultResponse(
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIdI6MjEwMCwiaWF0IjoxNjM4NjM2NDI3fQ.5zBBo9LMi9Y_L6gyN0WYq41Qn2GJGSySMs7XJ6c_aFk")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIdI6MjEwMCwiaWF0IjoxNjM4NjM2NDI3fQ.5zBBo9LMi9Y_L6gyN0WYq41Qn2GJGSySMs7XJ6c_aFk")
        String refreshToken
) {

    public static AuthResultResponse of(TokenPair tokenPair) {
        return new AuthResultResponse(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
