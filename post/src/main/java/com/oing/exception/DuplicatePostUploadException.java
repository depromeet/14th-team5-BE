package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class DuplicatePostUploadException extends DomainException {
    public DuplicatePostUploadException() {
        super(ErrorCode.DUPLICATE_POST_UPLOAD);
    }
}
