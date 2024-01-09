package com.oing.domain;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
