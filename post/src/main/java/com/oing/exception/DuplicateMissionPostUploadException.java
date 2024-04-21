package com.oing.exception;

public class DuplicateMissionPostUploadException extends DomainException {
    public DuplicateMissionPostUploadException() {
        super(ErrorCode.DUPLICATE_MISSION_POST_UPLOAD);
    }
}
