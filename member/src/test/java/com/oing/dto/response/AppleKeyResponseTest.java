package com.oing.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:04 PM
 */
class AppleKeyResponseTest {

    @DisplayName("AppleKeyResponse 생성자 테스트")
    @Test
    void testAppleKeyResponseConstructor() {
        // Given
        String kty = "RSA";
        String kid = "123456789";
        String use = "sig";
        String alg = "RS256";
        String n = "sampleNValue";
        String e = "sampleEValue";

        // When
        AppleKeyResponse keyResponse = new AppleKeyResponse(kty, kid, use, alg, n, e);

        // Then
        assertNotNull(keyResponse);
        assertEquals(kty, keyResponse.kty());
        assertEquals(kid, keyResponse.kid());
        assertEquals(use, keyResponse.use());
        assertEquals(alg, keyResponse.alg());
        assertEquals(n, keyResponse.n());
        assertEquals(e, keyResponse.e());
    }

    @DisplayName("AppleKeyResponse 생성자 테스트 - null")
    @Test
    void testAppleKeyResponseEquality() {
        // Given
        AppleKeyResponse keyResponse1 = new AppleKeyResponse("RSA", "123", "sig", "RS256", "n1", "e1");
        AppleKeyResponse keyResponse2 = new AppleKeyResponse("RSA", "123", "sig", "RS256", "n1", "e1");

        // Then
        assertEquals(keyResponse1, keyResponse2);
        assertEquals(keyResponse1.hashCode(), keyResponse2.hashCode());
    }
}
