package com.oing.exception;

public class MissionPostAccessDeniedFamilyException extends DomainException {
    public MissionPostAccessDeniedFamilyException() {
        super(ErrorCode.MISSION_POST_ACCESS_DENIED_FAMILY);
    }
}
