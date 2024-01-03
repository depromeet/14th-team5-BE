package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class AlreadyInFamilyException extends DomainException {
    public AlreadyInFamilyException() {
        super(ErrorCode.ALREADY_IN_FAMILY);
    }
}
