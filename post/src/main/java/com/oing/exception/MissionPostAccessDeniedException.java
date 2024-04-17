package com.oing.exception;

public class MissionPostAccessDeniedException extends DomainException {
    public MissionPostAccessDeniedException() {
        super(ErrorCode.MISSION_POST_ACCESS_DENIED);
    }
}
