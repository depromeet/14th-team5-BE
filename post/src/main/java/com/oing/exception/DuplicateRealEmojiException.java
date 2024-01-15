package com.oing.exception;

public class DuplicateRealEmojiException extends DomainException {
    public DuplicateRealEmojiException() {
        super(ErrorCode.DUPLICATE_REAL_EMOJI);
    }
}
