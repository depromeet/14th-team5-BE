package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:13 AM
 */
@Schema(description = "네이티브 소셜 로그인 요청")
public record NativeSocialLoginRequest(
        @NotBlank @Schema(description = "네이티브 로그인 절차 후 얻은 엑세스 토큰") String accessToken
) {
}
