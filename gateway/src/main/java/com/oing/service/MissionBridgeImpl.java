package com.oing.service;

import com.oing.domain.DailyMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class MissionBridgeImpl implements MissionBridge {

    private final MissionService missionService;
    private final DailyMissionHistoryService dailyMissionHistoryService;

    @Override
    public String getContentByMissionId(String missionId) {
        return missionService.getMissionByMissionId(missionId).getContent();
    }

    @Override
    public String getContentByDate(LocalDate date) {
        return missionService.getMissionByDate(date).content();
    }

    @Override
    public String getTodayMissionId() {
        LocalDate today = ZonedDateTime.now().toLocalDate();
        DailyMissionHistory todayMission = dailyMissionHistoryService.getDailyMissionHistoryByDate(today);

        return todayMission.getMission().getId();
    }
}
