package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class InvalidUploadTimeException extends DomainException {

    public InvalidUploadTimeException() {
        super(ErrorCode.INVALID_UPLOAD_TIME);
    }
}
