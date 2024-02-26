package com.oing.service;

import com.oing.domain.FamilyTopPercentageHistory;
import com.oing.domain.FamilyTopPercentageHistoryId;
import com.oing.repository.FamilyTopPercentageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FamilyTopPercentageHistoryService {

    private final FamilyTopPercentageHistoryRepository familyTopPercentageHistoryRepository;


    public int getTopPercentage(String familyId, int year, int month) {
        FamilyTopPercentageHistoryId familyTopPercentageHistoryId = new FamilyTopPercentageHistoryId(familyId, year, month);

        Optional<FamilyTopPercentageHistory> familyTopPercentageHistory = familyTopPercentageHistoryRepository.findByFamilyTopPercentageHistoryId(familyTopPercentageHistoryId);
        if (familyTopPercentageHistory.isEmpty()) { // 이력이 없다면, 최저값인 100 반환
            return 100;
        }

        return familyTopPercentageHistory.get().getTopPercentage();
    }
}
