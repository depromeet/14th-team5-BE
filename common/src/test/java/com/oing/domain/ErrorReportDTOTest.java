package com.oing.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 1:53 PM
 */
class ErrorReportDTOTest {

    @DisplayName("ErrorReportDTO 생성자 테스트")
    @Test
    void createErrorReportDTO() {
        // Given
        String errorMessage = "Test Error Message";
        String payload = "Test Payload";

        // When
        ErrorReportDTO errorReportDTO = new ErrorReportDTO(errorMessage, payload);

        // Then
        assertEquals(errorMessage, errorReportDTO.errorMessage());
        assertEquals(payload, errorReportDTO.payload());
    }

    @DisplayName("ErrorReportDTO toString 테스트")
    @Test
    void testToString() {
        // Given
        String errorMessage = "Test Error Message";
        String payload = "Test Payload";
        ErrorReportDTO errorReportDTO = new ErrorReportDTO(errorMessage, payload);

        // When
        String result = errorReportDTO.toString();

        // Then
        assertEquals("ErrorReportDTO[errorMessage=Test Error Message, payload=Test Payload]", result);
    }

    @DisplayName("ErrorReportDTO equals 테스트")
    @Test
    void testEquals() {
        // Given
        String errorMessage = "Test Error Message";
        String payload = "Test Payload";
        ErrorReportDTO errorReportDTO1 = new ErrorReportDTO(errorMessage, payload);
        ErrorReportDTO errorReportDTO2 = new ErrorReportDTO(errorMessage, payload);

        // When
        // Then
        assertEquals(errorReportDTO1, errorReportDTO2);
    }

    @DisplayName("ErrorReportDTO notEquals 테스트")
    @Test
    void testNotEquals() {
        // Given
        String errorMessage1 = "Test Error Message 1";
        String payload1 = "Test Payload 1";
        String errorMessage2 = "Test Error Message 2";
        String payload2 = "Test Payload 2";
        ErrorReportDTO errorReportDTO1 = new ErrorReportDTO(errorMessage1, payload1);
        ErrorReportDTO errorReportDTO2 = new ErrorReportDTO(errorMessage2, payload2);

        // When
        // Then
        assertNotEquals(errorReportDTO1, errorReportDTO2);
    }

    @DisplayName("ErrorReportDTO hashCode 테스트")
    @Test
    void testHashCode() {
        // Given
        String errorMessage = "Test Error Message";
        String payload = "Test Payload";
        ErrorReportDTO errorReportDTO1 = new ErrorReportDTO(errorMessage, payload);
        ErrorReportDTO errorReportDTO2 = new ErrorReportDTO(errorMessage, payload);

        // When
        // Then
        assertEquals(errorReportDTO1.hashCode(), errorReportDTO2.hashCode());
    }
}
