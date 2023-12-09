package com.oing.domain;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/6/23
 * Time: 8:13 PM
 */
@RequiredArgsConstructor
@Getter
public enum TokenType {
    ACCESS("access"), REFRESH("refresh"), TEMPORARY("temporary");
    private final String typeKey;

    public static TokenType fromString(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "ACCESS" -> ACCESS;
            case "REFRESH" -> REFRESH;
            case "TEMPORARY" -> TEMPORARY;
            default -> throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }
}
