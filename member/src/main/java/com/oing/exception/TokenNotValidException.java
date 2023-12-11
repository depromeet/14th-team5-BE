package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

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
