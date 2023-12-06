package com.oing.restapi;

import com.oing.dto.request.CreateNewMemberRequest;
import com.oing.dto.request.NativeSocialLoginRequest;
import com.oing.dto.request.RefreshAccessTokenRequest;
import com.oing.dto.response.AuthResultResponse;
import com.oing.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:04 AM
 */
@Tag(name = "인증 API", description = "인증(로그인) 관련 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
})
@RestController
@RequestMapping("/v1/auth")
public interface AuthApi {
    @Operation(summary = "네이티브 소셜 로그인", description = "네이티브(apple 등) 소셜 로그인을 진행합니다.")
    @PostMapping(value = "/social/{provider}")
    AuthResultResponse socialLogin(
            @PathVariable("provider") @Parameter(example = "APPLE", description = "oAuth 제공자 이름") String provider,
            @RequestBody @Valid NativeSocialLoginRequest request
    );

    @Operation(summary = "토큰 재발행", description = "리프레시 토큰으로 새로은 토큰을 발행합니다.")
    @PostMapping(value = "/refresh")
    AuthResultResponse refreshAccessToken(
            @RequestBody @Valid RefreshAccessTokenRequest request
    );

    @PreAuthorize("hasRole('TEMPORARY_MEMBER')")
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping(value = "/register")
    AuthResultResponse register(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody @Valid CreateNewMemberRequest request
    );
}
