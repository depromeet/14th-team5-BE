package com.oing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            default -> throw new InvalidParameterException();
        };
    }

    private static final List<Emoji> EMOJI_LIST = Arrays.asList(
            Emoji.EMOJI_1, Emoji.EMOJI_2, Emoji.EMOJI_3, Emoji.EMOJI_4, Emoji.EMOJI_5);

    public static List<Emoji> getEmojiList() {
        return Collections.unmodifiableList(EMOJI_LIST);
    }
}

