package com.oing.exception;

public class MissionNotFoundException extends DomainException {
    public MissionNotFoundException() {
        super(ErrorCode.MISSION_NOT_FOUND);
    }
}
