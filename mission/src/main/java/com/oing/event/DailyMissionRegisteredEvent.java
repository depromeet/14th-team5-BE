package com.oing.event;

import com.oing.domain.Mission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DailyMissionRegisteredEvent {
    private final Mission mission;
    private final Throwable throwable;
}
