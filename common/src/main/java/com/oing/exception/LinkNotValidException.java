package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class LinkNotValidException extends DomainException {
    public LinkNotValidException() {
        super(ErrorCode.LINK_NOT_VALID);
    }
}
