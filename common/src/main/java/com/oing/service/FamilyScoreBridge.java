package com.oing.service;


public interface FamilyScoreBridge {
    void updateAllFamilyTopPercentageHistories(int year, int month);

    int calculateFamilyTopPercentage(int rank, int familiesCount);
}
