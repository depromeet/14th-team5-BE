package com.oing.service;

import com.oing.domain.Family;
import com.oing.exception.FamilyNotFoundException;
import com.oing.repository.FamilyRepository;
import com.oing.util.DateUtils;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final FamilyTopPercentageHistoryService familyTopPercentageHistoryService;

    private final FamilyScoreBridge familyScoreBridge;
    private final IdentityGenerator identityGenerator;


    @Transactional
    public Family createFamily() {
        Family family = new Family(identityGenerator.generateIdentity(), null, null);
        return familyRepository.save(family);
    }

    public Family getFamilyById(String familyId) {
        return familyRepository
                .findById(familyId)
                .orElseThrow(FamilyNotFoundException::new);
    }

    @Transactional
    public Family getFamilyByIdWithLock(String familyId) {
        return familyRepository
                .findByIdWithLock(familyId)
                .orElseThrow(FamilyNotFoundException::new);
    }

    public int getFamilyTopPercentage(String familyId, LocalDate calendarDate) {
        if (DateUtils.isCurrentMonth(calendarDate)) {
            return calculateLiveFamilyTopPercentage(familyId);
        } else {
            return familyTopPercentageHistoryService.getTopPercentage(familyId, calendarDate.getYear(), calendarDate.getMonthValue());
        }
    }

    private int calculateLiveFamilyTopPercentage(String familyId) {
        int familiesCount = familyRepository.countByScoreDistinct();
        int score = getFamilyById(familyId).getScore();
        int rank = familyRepository.countByScoreGreaterThanEqualScoreDistinct(score);

        // score 를 통한 순위를 통해 전체 가족들 중 상위 백분율 계산 (1%에 가까울수록 고순위)
        return familyScoreBridge.calculateFamilyTopPercentage(rank, familiesCount);
    }

    @Transactional
    public Family updateFamilyName(String familyId, String loginMemberId, String familyName) {
        Family family = getFamilyById(familyId);
        family.updateFamilyName(familyName, loginMemberId);
        return family;
    }
}
