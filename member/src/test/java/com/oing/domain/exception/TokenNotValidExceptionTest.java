package com.oing.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenNotValidExceptionTest {
    @DisplayName("TokenNotValidException 생성자 테스트")
    @Test
    void testConstructor() {
        //given
        TokenNotValidException exception = new TokenNotValidException();

        //when
        Executable errorFunction = () -> {
          throw exception;
        };

        //then
        assertThrows(TokenNotValidException.class, errorFunction);
    }
}
