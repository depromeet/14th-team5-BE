package com.oing.exception;

public class EmojiNotFoundException extends DomainException {
    public EmojiNotFoundException() {
        super(ErrorCode.EMOJI_NOT_FOUND);
    }
}
