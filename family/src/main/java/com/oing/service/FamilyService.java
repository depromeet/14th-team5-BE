package com.oing.service;

import com.oing.domain.model.Family;
import com.oing.repository.FamilyRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final IdentityGenerator identityGenerator;

    @Transactional
    public Family createFamily() {
        Family family = new Family(identityGenerator.generateIdentity());
        return familyRepository.save(family);
    }
}
