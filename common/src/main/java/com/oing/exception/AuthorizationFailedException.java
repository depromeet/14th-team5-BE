package com.oing.exception;

public class AuthorizationFailedException extends DomainException {
    public AuthorizationFailedException() {
        super(ErrorCode.AUTHORIZATION_FAILED);
    }
}
