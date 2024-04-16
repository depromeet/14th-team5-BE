package com.oing.controller;

import com.oing.domain.DailyMissionHistory;
import com.oing.dto.response.DailyMissionResponse;
import com.oing.restapi.DailyMissionHistoryApi;
import com.oing.service.DailyMissionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZonedDateTime;


@Controller
@RequiredArgsConstructor
public class DailyMissionHistoryController implements DailyMissionHistoryApi {

    private final DailyMissionHistoryService dailyMissionHistoryService;

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
