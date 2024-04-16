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

        return DailyMissionHistoryResponse.from(dailyMissionHistory);
    }

    public DailyMissionHistory getDailyMissionHistoryByDate(LocalDate date) {
        // TODO: DailyMissionHistoryService 의 Feature Mocking 입니다.
        Mission mockMission = new Mission("1", "오늘의 기분을 나타내는 사진 찍기.");
        DailyMissionHistory mockDailyMissionHistory = new DailyMissionHistory(date, mockMission);

        return mockDailyMissionHistory;

        // TODO: Mocking 제거 시, 주석 해제
//        return dailyMissionHistoryRepository.findById(date)
//            .orElseThrow(DailyMissionHistoryNotFoundException::new);
    }
}
