package com.oing.service;

import com.oing.domain.CreateNewFamilyTopPercentageHistoryDTO;
import com.oing.domain.Family;
import com.oing.domain.FamilyTopPercentageHistory;
import com.oing.domain.MemberPost;
import com.oing.repository.FamilyRepository;
import com.oing.repository.FamilyTopPercentageHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.oing.domain.Family.*;

@Service
@RequiredArgsConstructor
public class FamilyScoreBridgeImpl implements FamilyScoreBridge {

    private final FamilyRepository familyRepository;
    private final FamilyTopPercentageHistoryRepository familyTopPercentageHistoryRepository;


    @Override
    @Transactional
    public void updateAllFamilyTopPercentageHistories(LocalDate historyDate) {
        List<Family> families = familyRepository.findAll();
        int familiesCount = familyRepository.countByScoreDistinct();

        for (Family family : families) {
            int rank = familyRepository.countByScoreGreaterThanEqualScoreDistinct(family.getScore());
            int topPercentage = calculateFamilyTopPercentage(rank, familiesCount);

            CreateNewFamilyTopPercentageHistoryDTO dto = new CreateNewFamilyTopPercentageHistoryDTO(
                    family.getId(),
                    historyDate,
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
