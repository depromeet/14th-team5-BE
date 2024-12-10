package com.oing.exception;

public class MemberVoiceCommentNotFoundException extends DomainException {
    public MemberVoiceCommentNotFoundException() {
        super(ErrorCode.VOICE_COMMENT_NOT_FOUND);
    }
}
