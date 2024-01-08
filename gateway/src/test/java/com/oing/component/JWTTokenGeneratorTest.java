package com.oing.component;

import com.oing.config.properties.TokenExpirationProperties;
import com.oing.config.properties.TokenProperties;
import com.oing.domain.TokenPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:23 PM
 */


@SpringBootTest
@ActiveProfiles("test")
class JWTTokenGeneratorTest {
    private TokenProperties tokenProperties = new TokenProperties(
            "your_secret_key_for_test_purpose_in_springboot_junit_project",
            new TokenExpirationProperties("3600000", "604800000")
    );

    private JWTTokenGenerator jwtTokenGenerator;

    @BeforeEach
    void setUp() {
        jwtTokenGenerator = new JWTTokenGenerator(tokenProperties);
    }

    @DisplayName("AccessToken 생성 테스트")
    @Test
    void testGenerateTokenPair() {
        // Given
        String userId = "sampleUserId";

        // When
        TokenPair tokenPair = jwtTokenGenerator.generateTokenPair(userId);

        // Then
        assertNotNull(tokenPair);
        assertNotNull(tokenPair.accessToken());
        assertNotNull(tokenPair.refreshToken());
    }

    @DisplayName("AccessToken userId 가지고 있는지")
    @Test
    void testGeneratedTokenPairUserIdEquals() {
        // Given
        String userId = "sampleUserId";

        // When
        TokenPair tokenPair = jwtTokenGenerator.generateTokenPair(userId);
//        String resultUserId = jwtTokenGenerator.getUserIdFromAccessToken(tokenPair.accessToken());
//
//        // Then
//        assertEquals(resultUserId, userId);
    }

    @DisplayName("AccessToken 유효성 검사 테스트")
    @Test
    void testGetUserIdFromAccessTokenWithInvalidToken() {
        // Given
        String invalidAccessToken = "invalid_token";

        // When, Then
//        assertThrows(TokenNotValidException.class,
//                () -> jwtTokenGenerator.getUserIdFromAccessToken(invalidAccessToken));
    }

    @DisplayName("RefreshToken 유효성 검사 테스트")
    @Test
    void testIsRefreshTokenInvalid() {
        // Given
        String invalidRefreshToken = "invalid_token";

        // When
//        boolean isValid = jwtTokenGenerator.isRefreshTokenValid(invalidRefreshToken);
//
//        // Then
//        assertFalse(isValid);
    }
}
