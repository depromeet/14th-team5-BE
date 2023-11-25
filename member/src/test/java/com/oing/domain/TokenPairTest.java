package com.oing.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenPairTest {
    @DisplayName("TokenPair 생성자 테스트")
    @Test
    void testConstructor() {
        //given
        String accessToken = "access-key";
        String refreshToken = "refresh-key";

        //when
        TokenPair tokenPair = new TokenPair(accessToken, refreshToken);

        //then
        assertEquals(tokenPair.accessToken(), accessToken);
        assertEquals(tokenPair.refreshToken(), refreshToken);
    }
}
