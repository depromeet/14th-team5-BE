package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class FamilyNotFoundException extends DomainException {
    public FamilyNotFoundException() {
        super(ErrorCode.FAMILY_NOT_FOUND);
    }
}
