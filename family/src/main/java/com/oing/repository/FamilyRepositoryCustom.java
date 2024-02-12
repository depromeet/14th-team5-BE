package com.oing.repository;

public interface FamilyRepositoryCustom {

    int countByScoreDistinct();

    int countByScoreGreaterThanEqualScoreDistinct(int familyScore);
}
