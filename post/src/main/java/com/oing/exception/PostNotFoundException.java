package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class PostNotFoundException extends DomainException {
    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
