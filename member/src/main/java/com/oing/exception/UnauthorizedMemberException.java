package com.oing.exception;

public class UnauthorizedMemberException extends AuthorizationFailedException {
    public UnauthorizedMemberException() {
        super(ErrorCode.UNAUTHORIZED_MEMBER_USED);
    }
}
