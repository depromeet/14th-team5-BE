package com.oing.exception;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:24 PM
 */
public class TokenExpiredException extends TokenNotValidException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}
