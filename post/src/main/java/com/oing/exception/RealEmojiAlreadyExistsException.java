package com.oing.exception;

public class RealEmojiAlreadyExistsException extends DomainException {
    public RealEmojiAlreadyExistsException() {
        super(ErrorCode.REAL_EMOJI_ALREADY_EXISTS);
    }
}
