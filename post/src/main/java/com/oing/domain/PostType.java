package com.oing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;

@RequiredArgsConstructor
@Getter
public enum PostType {

    SURVIVAL("survival"),
    MISSION("mission"),
    AI_IMAGE("ai_image");

    private final String typeKey;

    public static PostType fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "SURVIVAL" -> SURVIVAL;
            case "MISSION" -> MISSION;
            case "AI_IMAGE" -> AI_IMAGE;
            default -> throw new InvalidParameterException();
        };
    }
}
