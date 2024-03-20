package com.oing.service;

import com.oing.domain.CreateNewFamilyTopPercentageHistoryDTO;
import com.oing.domain.Family;
import com.oing.domain.FamilyTopPercentageHistory;
import com.oing.repository.FamilyRepository;
import com.oing.repository.FamilyTopPercentageHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyScoreBridgeImpl implements FamilyScoreBridge {

    private final FamilyRepository familyRepository;
    private final FamilyTopPercentageHistoryRepository familyTopPercentageHistoryRepository;


    @Override
    @Transactional
    public void updateAllFamilyTopPercentageHistories(int year, int month) {
        List<Family> families = familyRepository.findAll();
        int familiesCount = familyRepository.countByScoreDistinct();

        for (Family family : families) {
            int rank = familyRepository.countByScoreGreaterThanEqualScoreDistinct(family.getScore());
            int topPercentage = calculateFamilyTopPercentage(rank, familiesCount);

            CreateNewFamilyTopPercentageHistoryDTO dto = new CreateNewFamilyTopPercentageHistoryDTO(
                    family.getId(),
                    year,
                    month,
                    family,
                    topPercentage
            );
            FamilyTopPercentageHistory familyTopPercentageHistory = new FamilyTopPercentageHistory(dto);
            familyTopPercentageHistoryRepository.save(familyTopPercentageHistory);

            family.resetScore();
        }
    }

    @Override
    public int calculateFamilyTopPercentage(int rank, int familiesCount) {
        // divide by zero error 핸들링
        if (familiesCount == 0) {
            return 0;
        }

        return (int) Math.ceil((double) rank / familiesCount * 100);
    }
}
