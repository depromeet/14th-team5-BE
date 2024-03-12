package com.oing.repository;

import com.oing.domain.Family;

import java.util.Optional;

public interface FamilyRepositoryCustom {

    int countByScoreDistinct();

    int countByScoreGreaterThanEqualScoreDistinct(int familyScore);

    Optional<Family> findByIdWithLock(String familyId);
}
