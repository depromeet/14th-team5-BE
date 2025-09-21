package com.oing.exception;

public class AiImagePostLimitExceededException extends DomainException {
    public AiImagePostLimitExceededException() {
        super(ErrorCode.AI_IMAGE_POST_LIMIT_EXCEEDED);
    }
}
