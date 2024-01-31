package com.oing.service;

import com.oing.domain.Family;
import com.oing.exception.FamilyNotFoundException;
import com.oing.repository.FamilyRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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

    public int calculateFamilyTopPercentile(String familyId) {
        long allFamiliesCount = familyRepository.count();
        int familyScore = getFamilyById(familyId).getScore();
        long familyRank = familyRepository.countByScoreGreaterThanEqual(familyScore);

        return (100 - (int) ((familyRank / (double) allFamiliesCount) * 100));
    }
}
