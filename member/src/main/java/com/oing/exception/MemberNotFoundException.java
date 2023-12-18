package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class MemberNotFoundException extends DomainException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
