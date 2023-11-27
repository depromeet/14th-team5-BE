package com.oing.controller;

import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialLoginResult;
import com.oing.dto.request.NativeSocialLoginRequest;
import com.oing.dto.response.AuthResultResponse;
import com.oing.restapi.AuthApi;
import com.oing.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:04 AM
 */
@RequiredArgsConstructor
@Controller
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public AuthResultResponse socialLogin(String provider, NativeSocialLoginRequest request) {
        SocialLoginProvider socialLoginProvider = SocialLoginProvider.valueOf(provider.toUpperCase());
        SocialLoginResult socialLoginResult = authService
                .authenticateFromProvider(socialLoginProvider, request.accessToken());
        return null;
    }
}
