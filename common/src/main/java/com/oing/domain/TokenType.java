package com.oing.domain;

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
}
