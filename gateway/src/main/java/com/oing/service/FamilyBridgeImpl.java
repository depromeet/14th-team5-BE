package com.oing.service;

import com.oing.domain.Family;
import com.oing.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FamilyBridgeImpl implements FamilyBridge {
    private final FamilyRepository familyRepository;

    @Override
    public String findFamilyNameByFamilyId(String familyId) {
        return familyRepository.findById(familyId)
                .map(Family::getFamilyName)
                .orElse(null);
    }
}
