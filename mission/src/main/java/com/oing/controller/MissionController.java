package com.oing.controller;

import com.oing.domain.DailyMissionHistory;
import com.oing.domain.Mission;
import com.oing.dto.response.DailyMissionResponse;
import com.oing.dto.response.MissionResponse;
import com.oing.restapi.MissionApi;
import com.oing.service.DailyMissionHistoryService;
import com.oing.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZonedDateTime;


@Controller
@RequiredArgsConstructor
public class MissionController implements MissionApi {

    private final MissionService missionService;
    private final DailyMissionHistoryService dailyMissionHistoryService;


    @Override
    public MissionResponse getMissionByMissionId(String missionId) {
        Mission mission = missionService.getMissionByMissionId(missionId);
        return MissionResponse.from(mission);
    }

    @Override
    public DailyMissionResponse getTodayMission() {
        LocalDate today = ZonedDateTime.now().toLocalDate();

        DailyMissionHistory todayMissionHistory = dailyMissionHistoryService.getDailyMissionHistoryByDate(today);
        return new DailyMissionResponse(
                todayMissionHistory.getDate(),
                todayMissionHistory.getMission().getId(),
                todayMissionHistory.getMission().getContent()
        );
    }
}
