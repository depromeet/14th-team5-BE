package com.oing.exception;

public class AlreadyInFamilyException extends DomainException {
    public AlreadyInFamilyException() {
        super(ErrorCode.ALREADY_IN_FAMILY);
    }
}
