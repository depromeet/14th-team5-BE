package com.oing.config;

import com.oing.domain.TokenPair;
import com.oing.service.TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:25 PM
 */
@Component
public class JWTTokenGenerator implements TokenGenerator {
    @Override
    public TokenPair generateTokenPair(String userId) {
        return null;
    }

    @Override
    public String getUserIdFromAccessToken(String accessToken) {
        return null;
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        return false;
    }
}
