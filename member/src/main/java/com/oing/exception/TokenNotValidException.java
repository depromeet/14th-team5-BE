package com.oing.exception;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:24 PM
 */
public class TokenNotValidException extends DomainException {
    public TokenNotValidException() {
        super(ErrorCode.TOKEN_AUTHENTICATION_FAILED);
    }

    public TokenNotValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
