package com.oing.service;

import com.oing.domain.DailyMissionHistory;
import com.oing.domain.Mission;
import com.oing.dto.response.DailyMissionHistoryResponse;
import com.oing.exception.DailyMissionHistoryNotFoundException;
import com.oing.repository.DailyMissionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyMissionHistoryService {

    private final DailyMissionHistoryRepository dailyMissionHistoryRepository;


    public DailyMissionHistoryResponse createDailyMissionHistory(LocalDate date, Mission mission) {
        log.info("Create daily mission history : {}, missionId = {}", date, mission.getId());

        DailyMissionHistory dailyMissionHistory = DailyMissionHistory.builder()
                .date(date)
                .mission(mission)
                .build();
        dailyMissionHistory = dailyMissionHistoryRepository.save(dailyMissionHistory);

        return DailyMissionHistoryResponse.from(dailyMissionHistory);
    }

    public DailyMissionHistoryResponse registerTodayDailyMission(Mission mission) {
        LocalDate today = ZonedDateTime.now().toLocalDate();
        log.info("Create today's daily mission history: {}, missionId = {}", today, mission.getId());

        return createDailyMissionHistory(today, mission);
    }

    public DailyMissionHistory getDailyMissionHistoryByDate(LocalDate date) {
        return dailyMissionHistoryRepository.findById(date)
            .orElseThrow(DailyMissionHistoryNotFoundException::new);
    }

    public List<String> getRecentSevenDailyMissionIdsOrderByDateAsc() {
        return dailyMissionHistoryRepository.findRecentDailyMissionIdsOrderByDateAsc(7);
    }
}
