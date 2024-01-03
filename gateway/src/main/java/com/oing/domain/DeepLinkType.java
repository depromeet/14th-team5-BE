package com.oing.domain;

import lombok.RequiredArgsConstructor;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/3/24
 * Time: 10:13 AM
 */
@RequiredArgsConstructor
public enum DeepLinkType {
    FAMILY_REGISTRATION("FAMILY_REGISTRATION"),
    POST_VIEW("POST_VIEW"),
    ;
    private final String typeCode;
}
