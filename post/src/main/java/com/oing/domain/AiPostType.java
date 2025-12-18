package com.oing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;

@RequiredArgsConstructor
@Getter
public enum AiPostType {

    CHUSEOK_2025("chuseok_2025"),
    CHRISTMAS_2025("christmas_2025");

    private final String typeKey;

    public static AiPostType fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "CHUSEOK_2025" -> CHUSEOK_2025;
            case "CHRISTMAS_2025" -> CHRISTMAS_2025;
            default -> throw new InvalidParameterException();
        };
    }
}
