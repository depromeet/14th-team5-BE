package com.oing.exception;

public class FamilyNotFoundException extends DomainException {
    public FamilyNotFoundException() {
        super(ErrorCode.FAMILY_NOT_FOUND);
    }
}
