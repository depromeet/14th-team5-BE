package com.oing.exception;

public class MemberAlreadyExistsException extends DomainException {

    public MemberAlreadyExistsException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
