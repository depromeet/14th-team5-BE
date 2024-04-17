package com.oing.exception;

public class DuplicateSurvivalPostUploadException extends DomainException {
    public DuplicateSurvivalPostUploadException() {
        super(ErrorCode.DUPLICATE_SURVIVAL_POST_UPLOAD);
    }
}
