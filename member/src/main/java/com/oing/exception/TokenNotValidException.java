package com.oing.exception;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:24 PM
 */
public class TokenNotValidException extends DomainException {
    public TokenNotValidException() {
        super(ErrorCode.AUTHENTICATION_FAILED);
    }
}
