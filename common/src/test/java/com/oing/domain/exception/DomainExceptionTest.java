package com.oing.domain.exception;

import com.oing.exception.DomainException;
import com.oing.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
