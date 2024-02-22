package com.oing.service;

import java.time.LocalDate;

public interface FamilyScoreBridge {
    
    void updateAllFamilyTopPercentageHistories(LocalDate historyDate);

    int calculateFamilyTopPercentage(int rank, int familiesCount);
}
