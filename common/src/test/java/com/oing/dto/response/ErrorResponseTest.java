package com.oing.dto.response;

import com.oing.domain.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {
    @DisplayName("ErrorResponse.of(String, String) 메서드 테스트")
    @Test
    void testOfMethod() {
        //given
        String errorCode = "AU0001";
        String errorMessage = "Authentication failed";

        //when
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, errorMessage);

        //then
        assertEquals(errorResponse.code(), errorCode);
        assertEquals(errorResponse.message(), errorMessage);
    }

    @DisplayName("ErrorResponse.of(ErrorCode) 메서드 테스트")
    @Test
    void testOfMethod2() {
        //given
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;

        //when
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        //then
        assertEquals(errorResponse.code(), errorCode.getCode());
        assertEquals(errorResponse.message(), errorCode.getMessage());
    }
}
