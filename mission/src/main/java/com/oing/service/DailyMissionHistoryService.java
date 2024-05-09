package com.oing.service;

import com.oing.domain.DailyMissionHistory;
import com.oing.domain.Mission;
import com.oing.dto.response.DailyMissionHistoryResponse;
import com.oing.exception.DailyMissionHistoryNotFoundException;
import com.oing.exception.DuplicatedDailyMissionHistoryException;
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

    public DailyMissionHistoryResponse registerTodayDailyMission(Mission mission) throws DuplicatedDailyMissionHistoryException {
        LocalDate today = ZonedDateTime.now().toLocalDate();

        if (dailyMissionHistoryRepository.existsById(today)) {
            log.error("Today's daily mission history already exists: {}", today);
            throw new DuplicatedDailyMissionHistoryException();
        }

        log.info("Create today's daily mission : {}, missionId = {}", today, mission.getId());
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
