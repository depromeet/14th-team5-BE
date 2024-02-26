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
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final FamilyTopPercentageHistoryService familyTopPercentageHistoryService;

    private final FamilyScoreBridge familyScoreBridge;
    private final IdentityGenerator identityGenerator;

    public ZonedDateTime findFamilyCreatedAt(String familyId) {
        Family family = findFamilyById(familyId);
        return convertCreatedAtToZonedDateTime(family);
    }

    private Family findFamilyById(String familyId) {
        return familyRepository
                .findById(familyId)
                .orElseThrow(FamilyNotFoundException::new);
    }

    private ZonedDateTime convertCreatedAtToZonedDateTime(Family family) {
        Instant createdAtInstant = family.getCreatedAt().toInstant(ZoneOffset.ofHours(9));
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return ZonedDateTime.ofInstant(createdAtInstant, zoneId);
    }

    @Transactional
    public Family createFamily() {
        Family family = new Family(identityGenerator.generateIdentity());
        return familyRepository.save(family);
    }

    @Transactional
    public Family getFamilyById(String familyId) {
        return familyRepository
                .findById(familyId)
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
}
