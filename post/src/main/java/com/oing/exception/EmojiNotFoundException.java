package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class EmojiNotFoundException extends DomainException {
    public EmojiNotFoundException() {
        super(ErrorCode.EMOJI_NOT_FOUND);
    }
}
