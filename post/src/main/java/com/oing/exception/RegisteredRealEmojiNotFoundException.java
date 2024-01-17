package com.oing.exception;

public class RegisteredRealEmojiNotFoundException extends DomainException {
    public RegisteredRealEmojiNotFoundException() {
        super(ErrorCode.REGISTERED_REAL_EMOJI_NOT_FOUND);
    }
}
