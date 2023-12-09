package com.oing.dto.response;

import com.oing.domain.TokenPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:04 PM
 */
class AuthResultResponseTest {

    @DisplayName("AuthResultResponse 생성자 테스트")
    @Test
    void testAuthResultResponseConstructor() {
        // Given
        String accessToken = "sampleAccessToken";
        String refreshToken = "sampleRefreshToken";

        // When
        AuthResultResponse authResultResponse = new AuthResultResponse(accessToken, refreshToken, false);

        // Then
        assertNotNull(authResultResponse);
        assertEquals(accessToken, authResultResponse.accessToken());
        assertEquals(refreshToken, authResultResponse.refreshToken());
    }

    @DisplayName("AuthResultResponse 생성자 테스트 - null")
    @Test
    void testAuthResultResponseOf() {
        // Given
        TokenPair tokenPair = new TokenPair("sampleAccessToken", "sampleRefreshToken");

        // When
        AuthResultResponse authResultResponse = AuthResultResponse.of(tokenPair, false);

        // Then
        assertNotNull(authResultResponse);
        assertEquals(tokenPair.accessToken(), authResultResponse.accessToken());
        assertEquals(tokenPair.refreshToken(), authResultResponse.refreshToken());
    }
}
