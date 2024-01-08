package com.oing.exception;

public class InvalidUploadTimeException extends DomainException {

    public InvalidUploadTimeException() {
        super(ErrorCode.INVALID_UPLOAD_TIME);
    }
}
