package com.oing.service;

import com.oing.domain.DailyMissionHistory;
import com.oing.domain.Mission;
import com.oing.dto.response.DailyMissionHistoryResponse;
import com.oing.exception.DuplicatedDailyMissionHistoryException;
import com.oing.repository.DailyMissionHistoryRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyMissionHistoryServiceTest {

    @InjectMocks
    private DailyMissionHistoryService dailyMissionHistoryService;

    @Mock
    private DailyMissionHistoryRepository dailyMissionHistoryRepository;


    @Nested
    class registerTodayDailyMission {
        @Test
        void 정상_생성_테스트() {
            // given
            Mission mission = Mission.builder()
                    .id("testMission1")
                    .content("testMission1")
                    .build();

            when(dailyMissionHistoryRepository.existsById(any(LocalDate.class))).thenReturn(false);
            when(dailyMissionHistoryRepository.save(any(DailyMissionHistory.class))).thenReturn(DailyMissionHistory.builder()
                    .date(LocalDate.now())
                    .mission(mission)
                    .build());

            // when
            DailyMissionHistoryResponse dailyMissionHistoryResponse = dailyMissionHistoryService.registerTodayDailyMission(mission);

            // then
            assertThat(dailyMissionHistoryResponse)
                    .extracting(DailyMissionHistoryResponse::date, DailyMissionHistoryResponse::missionId)
                    .containsExactly(LocalDate.now(), mission.getId());
        }

        @Test
        void 이미_일일미션이_존재하는경우_예외를_던진다() {
            // given
            Mission mission = Mission.builder()
                    .id("testMission1")
                    .content("testMission1")
                    .build();

            when(dailyMissionHistoryRepository.existsById(any(LocalDate.class))).thenReturn(true);

            // when & then
            assertThatExceptionOfType(DuplicatedDailyMissionHistoryException.class)
                    .isThrownBy(() -> dailyMissionHistoryService.registerTodayDailyMission(mission));
        }
    }
}
