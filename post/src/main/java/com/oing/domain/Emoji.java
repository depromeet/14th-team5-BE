package com.oing.domain;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Emoji {

    EMOJI_1("emoji_1"),
    EMOJI_2("emoji_2"),
    EMOJI_3("emoji_3"),
    EMOJI_4("emoji_4"),
    EMOJI_5("emoji_5");

    private final String typeKey;

    public static Emoji fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "EMOJI_1" -> EMOJI_1;
            case "EMOJI_2" -> EMOJI_2;
            case "EMOJI_3" -> EMOJI_3;
            case "EMOJI_4" -> EMOJI_4;
            case "EMOJI_5" -> EMOJI_5;
            default -> throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }
}

