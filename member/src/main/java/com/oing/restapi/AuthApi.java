package com.oing.restapi;

import com.oing.dto.request.NativeSocialLoginRequest;
import com.oing.dto.response.AuthResultResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:04 AM
 */
@Tag(name = "인증 API", description = "인증(로그인) 관련 API")
@RestController
@RequestMapping("/v1/auth")
public interface AuthApi {
    @PostMapping(value = "/social")
    AuthResultResponse socialLogin(
            @RequestParam("provider") String provider,
            @RequestBody @Valid NativeSocialLoginRequest request
    );
}
