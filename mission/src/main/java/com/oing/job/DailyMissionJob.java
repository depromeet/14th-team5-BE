package com.oing.job;

import com.oing.domain.Mission;
import com.oing.dto.response.DailyMissionHistoryResponse;
import com.oing.service.DailyMissionHistoryService;
import com.oing.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyMissionJob {

    private final MissionService missionService;
    private final DailyMissionHistoryService dailyMissionHistoryService;


    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul") // 매일 01시
    @SchedulerLock(name = "DailyMissionRegistrationJob", lockAtMostFor = "PT30S", lockAtLeastFor = "PT30S")
    public void registerDailyMission() {
        log.info("[DailyMissionRegistrationJob] 일일 미션 등록 시작");

        List<String> recentDailyMissionIds = dailyMissionHistoryService.getRecentSevenDailyMissionIdsOrderByDateAsc();
        Mission newDailyMission = missionService.getRandomMissionExcludingIds(recentDailyMissionIds)
                .orElse(missionService.getMissionByMissionId(recentDailyMissionIds.get(0))); // 최근 7일 동안 등록된 일일 미션 제외했을 때, 미션이 없다면 가장 오래된 미션을 가져옴

        DailyMissionHistoryResponse dailyMissionHistoryResponse = dailyMissionHistoryService.registerTodayDailyMission(newDailyMission);
        log.info("[DailyMissionRegistrationJob] 일일 미션 등록 성공 - {}, registeredMissionId = {}", dailyMissionHistoryResponse.date(), dailyMissionHistoryResponse.missionId());
    }
}
