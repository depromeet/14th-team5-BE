package com.oing.service;

import com.oing.domain.TokenPair;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:16 PM
 */
public interface TokenGenerator {
    /**
     * 토큰 페어 (엑세스토큰, 리프레시 토큰)을 생성합니다.
     * @param userId 사용자 아이디
     * @return 토큰 페어 (엑세스 토큰, 리프레시 토큰)
     */
    TokenPair generateTokenPair(String userId);

    /**
     * 엑세스 토큰에서 유저ID를 추출합니다.
     * 유효하지 않은 경우, TokenNotValidException 이 발생합니다.
     * @throws com.oing.domain.exception.TokenNotValidException 토큰이 유효하지 않은 경우
     * @param accessToken 엑세스 토큰
     * @return 유저 아이디
     */
    String getUserIdFromAccessToken(String accessToken);

    /**
     * 리프레시 토큰 유효성을 검증합니다.
     * 유효하다면 true, 유효하지 않다면 false 를 반환합니다.
     * @param refreshToken 리프레시 토큰
     * @return 리프레시 토큰 유효성
     */
    boolean isRefreshTokenValid(String refreshToken);
}
