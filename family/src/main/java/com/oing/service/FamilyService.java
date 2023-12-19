package com.oing.service;

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
        return familyRepository
                .findById(familyId)
                .map(family -> {
                    Instant createdAtInstant = family.getCreatedAt().toInstant(ZoneOffset.ofHours(9));
                    ZoneId zoneId = ZoneId.systemDefault();
                    return ZonedDateTime.ofInstant(createdAtInstant, zoneId);
                })
                .orElseThrow(FamilyNotFoundException::new);
    }
}
