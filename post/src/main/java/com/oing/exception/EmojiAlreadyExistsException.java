package com.oing.exception;

public class EmojiAlreadyExistsException extends DomainException {
    public EmojiAlreadyExistsException() {
        super(ErrorCode.EMOJI_ALREADY_EXISTS);
    }
}
