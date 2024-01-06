package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class MemberAlreadyExistsException extends DomainException {

    public MemberAlreadyExistsException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
}
