package com.oing.exception;

public class MissionPostCreateAccessDeniedMemberException extends DomainException {
    public MissionPostCreateAccessDeniedMemberException() {
        super(ErrorCode.MISSION_POST_CREATE_ACCESS_DENIED_MEMBER);
    }
}
