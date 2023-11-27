package com.oing.service;

import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialLoginResult;
import org.springframework.stereotype.Service;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:18 AM
 */
@Service
public class AuthService {
    public SocialLoginResult authenticateFromProvider(SocialLoginProvider provider, String accessToken) {
        return switch (provider) {
            case APPLE -> authenticateFromApple(accessToken);
        };
    }

    private SocialLoginResult authenticateFromApple(String accessToken) {
        return null;
    }
}
