package com.oing.exception;

public class InvalidMemberNameLengthException extends DomainException {
    public InvalidMemberNameLengthException() {
        super(ErrorCode.INVALID_MEMBER_NAME_LENGTH);
    }
}
