package com.oing.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:14 PM
 */
class ErrorCodeTest {
    @DisplayName("ErrorCode valueOf 테스트")
    @Test
    void testErrorCodeValueOf() {
        // Given

        // When
        ErrorCode unknownServerError = ErrorCode.valueOf("UNKNOWN_SERVER_ERROR");

        // Then
        assertEquals("CM0001", unknownServerError.getCode());
        assertEquals("Unknown Server Error", unknownServerError.getMessage());
    }

    @DisplayName("ErrorCode getter 테스트")
    @Test
    void testErrorCodeGetters() {
        // Given
        ErrorCode unknownServerError = ErrorCode.UNKNOWN_SERVER_ERROR;

        // Then
        assertEquals("CM0001", unknownServerError.getCode());
        assertEquals("Unknown Server Error", unknownServerError.getMessage());
    }
}
