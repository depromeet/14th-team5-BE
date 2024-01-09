package com.oing.exception;

public class PostNotFoundException extends DomainException {
    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
