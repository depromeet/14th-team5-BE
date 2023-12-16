package com.oing.domain;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Emoji {

    HEART("heart"), SLIGHTLY_SMILING_FACE("slightly_smiling_face"), SHINING_FACE("shining_face"),
    SMILING_FACE("smiling_face"), SMILE("smile");

    private final String typeKey;

    public static Emoji fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "HEART" -> HEART;
            case "SLIGHTLY_SMILING_FACE" -> SLIGHTLY_SMILING_FACE;
            case "SHINING_FACE" -> SHINING_FACE;
            case "SMILING_FACE" -> SMILING_FACE;
            case "SMILE" -> SMILE;
            default -> throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }
}

