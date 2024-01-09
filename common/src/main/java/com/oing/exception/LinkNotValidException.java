package com.oing.exception;

public class LinkNotValidException extends DomainException {
    public LinkNotValidException() {
        super(ErrorCode.LINK_NOT_VALID);
    }
}
