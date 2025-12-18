package com.oing.exception;

public class MissingAiPostTypeException extends DomainException {
    public MissingAiPostTypeException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
