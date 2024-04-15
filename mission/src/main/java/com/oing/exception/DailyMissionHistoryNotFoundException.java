package com.oing.exception;

public class DailyMissionHistoryNotFoundException extends DomainException {

    public DailyMissionHistoryNotFoundException() {
        super(ErrorCode.DAILY_MISSION_HISTORY_NOT_FOUND);
    }
}
