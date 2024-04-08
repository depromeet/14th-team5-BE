package com.oing.exception;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/2/24
 * Time: 12:04 PM
 */
public class PickFailedAlreadyUploadedException extends DomainException {
    public PickFailedAlreadyUploadedException() {
        super(ErrorCode.MEMBER_ALREADY_UPLOADED_POST);
    }
}
