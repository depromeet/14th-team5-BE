package com.oing.service;

import com.oing.domain.Family;
import com.oing.exception.FamilyNotFoundException;
import com.oing.repository.FamilyRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
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

    public int getFamilyTopPercentage(String familyId, LocalDate date) {
        // 이번 달의 캘린더를 조회 시, 실시간으로 topPercentage를 계산
        if (date.getYear() == LocalDate.now().getYear() && date.getMonth() == LocalDate.now().getMonth()) {
            return calculateLiveFamilyTopPercentage(familyId);

        // 과거의 캘린더를 조회 시, 해당 날짜의 topPercentage를 조회
        } else {
            return getFamilyTopPercentageHistory(familyId, date);
        }
    }

    private int calculateLiveFamilyTopPercentage(String familyId) {
        long allFamiliesCount = familyRepository.count();
        int familyScore = getFamilyById(familyId).getScore();
        long familyRank = familyRepository.countByScoreGreaterThanEqual(familyScore);

        // divide by zero error 핸들링
        if (allFamiliesCount == 0) {
            return 0;
        }

        // score 를 통한 순위를 통해 전체 가족들 중 상위 백분율 계산 (0%에 가까울수록 고순위)
        return (int) ((familyRank / (double) allFamiliesCount) * 100);
    }

    private int getFamilyTopPercentageHistory(String familyId, LocalDate date) {
//        return familyTopPercentageHistoryService.getTopPercentageByFamilyIdAndDate(familyId, date);
        return 0;
    }
}
