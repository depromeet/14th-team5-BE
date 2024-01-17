package com.oing.exception;

public class RealEmojiNotFoundException extends DomainException {
    public RealEmojiNotFoundException() {
        super(ErrorCode.REAL_EMOJI_NOT_FOUND);
    }
}
