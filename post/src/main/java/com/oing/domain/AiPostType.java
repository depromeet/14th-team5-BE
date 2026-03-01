package com.oing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public enum AiPostType {

    CHUSEOK_2025("chuseok_2025", LocalDate.of(2025, 9, 29), LocalDate.of(2025, 10, 27)),
    CHRISTMAS_2025("christmas_2025", LocalDate.of(2025, 12, 23), LocalDate.of(2025, 12, 31));

    private final String typeKey;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public static AiPostType fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "CHUSEOK_2025" -> CHUSEOK_2025;
            case "CHRISTMAS_2025" -> CHRISTMAS_2025;
            default -> throw new InvalidParameterException();
        };
    }
}
