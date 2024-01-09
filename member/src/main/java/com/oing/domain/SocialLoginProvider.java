package com.oing.domain;

import java.security.InvalidParameterException;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:22 AM
 */
public enum SocialLoginProvider {
    APPLE, //애플 로그인
    KAKAO, //카카오 로그인
    GOOGLE, //구글 로그인
    INTERNAL; //내부 로그인

    public static SocialLoginProvider fromString(String provider) {
        return switch (provider.toUpperCase()) {
            case "APPLE" -> APPLE;
            case "INTERNAL" -> INTERNAL;
            case "KAKAO" -> KAKAO;
            case "GOOGLE" -> GOOGLE;
            default -> throw new InvalidParameterException();
        };
    }
}
