package com.oing.job;

import com.oing.service.FamilyScoreBridge;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class FamilyStatisticJob {

    private final FamilyScoreBridge familyScoreBridge;

    @Scheduled(cron = "0 0 1 1 * *", zone = "Asia/Seoul") // 매월 1일 01시
    @SchedulerLock(name = "MonthlyFamilyTopPercentageHistoryRecordingSchedule", lockAtMostFor = "PT30S", lockAtLeastFor = "PT30S")
    public void recordAllFamilyTopPercentageHistoriesMonthly() {
        // 방금 막 지나간 지난 달의 데이터를 기록한다.
        LocalDate historyDate = LocalDate.now().minusMonths(1);
        int historyYear = historyDate.getYear();
        int historyMonth = historyDate.getMonthValue();

        log.info("[MonthlyFamilyTopPercentageHistoryRecordingSchedule: {}-{}] scheduled and locked for 30s", historyYear, historyMonth);

        familyScoreBridge.updateAllFamilyTopPercentageHistories(historyYear, historyMonth);
    }
}
