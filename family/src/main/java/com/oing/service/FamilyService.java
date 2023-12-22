package com.oing.service;

import com.oing.domain.model.Family;
import com.oing.exception.FamilyNotFoundException;
import com.oing.repository.FamilyRepository;
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
}
