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
public class NativeSocialLoginRequestTest {

    @DisplayName("NativeSocialLoginRequest 생성자 테스트")
    @Test
    void testNativeSocialLoginRequestConstructor() {
        // Given
        String accessToken = "sampleAccessToken";

        // When
        NativeSocialLoginRequest request = new NativeSocialLoginRequest(accessToken);

        // Then
        assertNotNull(request);
        assertEquals(accessToken, request.accessToken());
    }

    @DisplayName("NativeSocialLoginRequest setter 테스트")
    @Test
    void testNativeSocialLoginRequestValidation() {
        // Given
        String validAccessToken = "sampleAccessToken";
        String invalidAccessToken = "";

        // When
        NativeSocialLoginRequest validRequest = new NativeSocialLoginRequest(validAccessToken);
        NativeSocialLoginRequest invalidRequest = new NativeSocialLoginRequest(invalidAccessToken);

        // Then
        assertDoesNotThrow(() -> validateRequest(validRequest));
        assertThrows(IllegalArgumentException.class, () -> validateRequest(invalidRequest));
    }

    private void validateRequest(NativeSocialLoginRequest request) {
        if (request.accessToken().isEmpty()) {
            throw new IllegalArgumentException("Access token cannot be empty");
        }
    }
}
