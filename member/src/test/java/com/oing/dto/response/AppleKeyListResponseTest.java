package com.oing.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:03 PM
 */
class AppleKeyListResponseTest {

    @DisplayName("AppleKeyListResponse 생성자 테스트")
    @Test
    void testAppleKeyListResponseConstructor() {
        // Given
        AppleKeyResponse[] keys = {};

        // When
        AppleKeyListResponse response = new AppleKeyListResponse(keys);

        // Then
        assertNotNull(response);
        assertArrayEquals(keys, response.keys());
    }

    @DisplayName("AppleKeyListResponse 생성자 테스트 - null")
    @Test
    void testAppleKeyListResponseEmptyConstructor() {
        // When
        AppleKeyListResponse response = new AppleKeyListResponse(new AppleKeyResponse[0]);

        // Then
        assertNotNull(response);
        assertNotNull(response.keys());
        assertEquals(0, response.keys().length);
    }
}
