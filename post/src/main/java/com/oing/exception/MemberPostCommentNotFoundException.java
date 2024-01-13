package com.oing.exception;

public class MemberPostCommentNotFoundException extends DomainException {
    public MemberPostCommentNotFoundException() {
        super(ErrorCode.POST_COMMENT_NOT_FOUND);
    }
}
