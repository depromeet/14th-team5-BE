package com.oing.job;

import com.oing.service.FamilyScoreBridge;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FamilyStatisticJob {

    private final FamilyScoreBridge familyScoreBridge;

    @Scheduled(cron = "0 0 1 1 * *") // 매월 1일 01시
    public void recordAllFamilyTopPercentageHistoriesMonthly() {
        LocalDate historyDate = LocalDate.now().minusMonths(1);
        familyScoreBridge.updateAllFamilyTopPercentageHistories(historyDate);
    }
}
