package com.oing.exception;

public class FamilyIdNotFoundException extends DomainException {
    public FamilyIdNotFoundException() {
        super(ErrorCode.FAMILY_NOT_FOUND);
    }
}
