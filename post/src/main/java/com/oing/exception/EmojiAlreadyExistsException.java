package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

public class EmojiAlreadyExistsException extends DomainException {
    public EmojiAlreadyExistsException() {
        super(ErrorCode.EMOJI_ALREADY_EXISTS);
    }
}
