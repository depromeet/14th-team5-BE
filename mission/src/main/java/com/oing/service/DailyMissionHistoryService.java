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

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyMissionHistoryService {

    private final DailyMissionHistoryRepository dailyMissionHistoryRepository;


    public DailyMissionHistoryResponse createDailyMissionHistory(LocalDate date, Mission mission) {
        log.info("Create daily mission history request: {}, missionId = {}", date, mission.getId());

        DailyMissionHistory dailyMissionHistory = DailyMissionHistory.builder()
                .date(date)
                .mission(mission)
                .build();
        dailyMissionHistory = dailyMissionHistoryRepository.save(dailyMissionHistory);

        return DailyMissionHistoryResponse.from(dailyMissionHistoryRepository.save(dailyMissionHistory));
    }

    public DailyMissionHistory getDailyMissionHistoryByDate(LocalDate date) {
        return dailyMissionHistoryRepository.findById(date)
            .orElseThrow(DailyMissionHistoryNotFoundException::new);
    }
}
