package com.oing.dto.response;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 12:23 PM
 */
public record AppleKeyResponse(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {
}
