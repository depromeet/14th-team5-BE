package com.oing.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:01 PM
 */
class RefreshAccessTokenRequestTest {

    @DisplayName("RefreshAccessTokenRequest 생성자 테스트")
    @Test
    void testRefreshAccessTokenRequestConstructor() {
        // Given
        String accessToken = "sampleAccessToken";

        // When
        RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(accessToken);

        // Then
        assertNotNull(request);
        assertEquals(accessToken, request.refreshToken());
    }

    @DisplayName("RefreshAccessTokenRequest setter 테스트")
    @Test
    void testRefreshAccessTokenRequestValidation() {
        // Given
        String validAccessToken = "sampleAccessToken";
        String invalidAccessToken = "";

        // When
        RefreshAccessTokenRequest validRequest = new RefreshAccessTokenRequest(validAccessToken);
        RefreshAccessTokenRequest invalidRequest = new RefreshAccessTokenRequest(invalidAccessToken);

        // Then
        assertDoesNotThrow(() -> validateRequest(validRequest));
        assertThrows(IllegalArgumentException.class, () -> validateRequest(invalidRequest));
    }

    private void validateRequest(RefreshAccessTokenRequest request) {
        if (request.refreshToken().isEmpty()) {
            throw new IllegalArgumentException("Access token cannot be empty");
        }
    }
}
