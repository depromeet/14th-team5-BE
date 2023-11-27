package com.oing.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:07 PM
 */
class SocialLoginResultTest {

    @DisplayName("SocialLoginResult 생성자 테스트")
    @Test
    void testSocialLoginResultConstructor() {
        // Given
        String identifier = "user123";

        // When
        SocialLoginResult socialLoginResult = new SocialLoginResult(identifier);

        // Then
        assertNotNull(socialLoginResult);
        assertEquals(identifier, socialLoginResult.identifier());
    }
}
