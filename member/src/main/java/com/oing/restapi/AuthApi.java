package com.oing.restapi;

import com.oing.dto.request.NativeSocialLoginRequest;
import com.oing.dto.response.AuthResultResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(value = "/social", params = "type=APPLE")
    AuthResultResponse socialLoginWithNativeApple(@RequestBody @Valid NativeSocialLoginRequest request);
}
