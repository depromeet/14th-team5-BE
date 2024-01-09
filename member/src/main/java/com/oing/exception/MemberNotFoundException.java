package com.oing.exception;

public class MemberNotFoundException extends DomainException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
