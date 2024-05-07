package com.oing.exception;

public class DuplicatedDailyMissionHistoryException extends DomainException {

    public DuplicatedDailyMissionHistoryException() {
        super(ErrorCode.DUPLICATED_DAILY_MISSION_HISTORY);
    }
}
