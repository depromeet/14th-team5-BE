package com.oing.exception;

public class AuthorizationFailedException extends DomainException {
    public AuthorizationFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthorizationFailedException() {
        super(ErrorCode.AUTHORIZATION_FAILED);
    }
}
