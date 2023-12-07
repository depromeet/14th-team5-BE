package com.oing.domain;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:22 AM
 */
public enum SocialLoginProvider {
    APPLE, //애플 로그인
    INTERNAL; //내부 로그인

    public static SocialLoginProvider fromString(String provider) {
        return switch (provider.toUpperCase()) {
            case "APPLE" -> APPLE;
            case "INTERNAL" -> INTERNAL;
            default -> throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }
}
