package com.oing.domain;

public record Token(
        String userId,
        TokenType tokenType
) {
}
