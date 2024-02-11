package com.oing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FamilyStatisticSchedulerService {

    private final FamilyScoreBridge familyScoreBridge;

    @Scheduled(cron = "0 1 1 * * *")
    public void recordAllFamilyTopPercentageHistoriesMonthly() {
        LocalDate historyDate = LocalDate.now().minusMonths(1);
        familyScoreBridge.updateAllFamilyTopPercentageHistories(historyDate);
    }
}
