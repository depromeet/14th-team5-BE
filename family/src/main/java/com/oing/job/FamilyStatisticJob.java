package com.oing.job;

import com.oing.service.FamilyScoreBridge;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FamilyStatisticJob {

    private final FamilyScoreBridge familyScoreBridge;

    @Scheduled(cron = "0 0 1 1 * *") // 매월 1일 01시
    @SchedulerLock(name = "MonthlyFamilyTopPercentageHistoryRecordingScheduling", lockAtMostFor = "PT30S", lockAtLeastFor = "PT30S")
    public void recordAllFamilyTopPercentageHistoriesMonthly() {
        // 방금 막 지나간 지난 달의 데이터를 기록한다.
        LocalDate historyDate = LocalDate.now().minusMonths(1);
        familyScoreBridge.updateAllFamilyTopPercentageHistories(historyDate.getYear(), historyDate.getMonthValue());
    }
}
