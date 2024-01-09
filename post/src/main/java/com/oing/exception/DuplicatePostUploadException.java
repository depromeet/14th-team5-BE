package com.oing.exception;

public class DuplicatePostUploadException extends DomainException {
    public DuplicatePostUploadException() {
        super(ErrorCode.DUPLICATE_POST_UPLOAD);
    }
}
