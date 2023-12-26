package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class AuthorizationFailedException extends DomainException {
    public AuthorizationFailedException() {
        super(ErrorCode.AUTHORIZATION_FAILED);
    }
}
