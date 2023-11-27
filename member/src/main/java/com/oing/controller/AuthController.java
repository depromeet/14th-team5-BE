package com.oing.controller;

import com.oing.dto.request.NativeSocialLoginRequest;
import com.oing.dto.response.AuthResultResponse;
import com.oing.restapi.AuthApi;
import org.springframework.stereotype.Controller;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:04 AM
 */
@Controller
public class AuthController implements AuthApi {
    @Override
    public AuthResultResponse socialLoginWithNativeApple(NativeSocialLoginRequest request) {
        return null;
    }
}
