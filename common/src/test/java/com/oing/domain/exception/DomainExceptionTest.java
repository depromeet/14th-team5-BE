package com.oing.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class DomainExceptionTest {
    @DisplayName("DomainException 생성자 테스트")
    @Test
    void testConstructor() {
        //given
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;

        //when
        DomainException exception = new DomainException(errorCode);

        //then
        assertEquals(exception.getErrorCode(), errorCode);
    }
}
