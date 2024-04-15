package com.oing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;

@RequiredArgsConstructor
@Getter
public enum Type {

    SURVIVAL("survival"),
    MISSION("mission");

    private final String typeKey;

    public static Type fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "SURVIVAL" -> SURVIVAL;
            case "MISSION" -> MISSION;
            default -> throw new InvalidParameterException();
        };
    }
}
