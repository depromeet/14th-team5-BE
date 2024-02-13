package com.oing.service;

import com.oing.domain.CreateNewFamilyTopPercentageHistoryDTO;
import com.oing.domain.FamilyTopPercentageHistory;
import com.oing.domain.FamilyTopPercentageHistoryId;
import com.oing.repository.FamilyTopPercentageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FamilyTopPercentageHistoryService {

    private final FamilyTopPercentageHistoryRepository familyTopPercentageHistoryRepository;


    public int getTopPercentageByFamilyIdAndDate(String familyId, LocalDate historyDate) {
        LocalDate firstDayOfMonth = historyDate.withDayOfMonth(1);
        FamilyTopPercentageHistoryId familyTopPercentageHistoryId = new FamilyTopPercentageHistoryId(familyId, firstDayOfMonth);


        Optional<FamilyTopPercentageHistory> familyTopPercentageHistory = familyTopPercentageHistoryRepository.findByFamilyTopPercentageHistoryId(familyTopPercentageHistoryId);
        // 이력이 없다면, 최저값인 100 반환
        if (familyTopPercentageHistory.isEmpty()) {
            return 100;
        }

        return familyTopPercentageHistory.get().getTopPercentage();
    }
}
