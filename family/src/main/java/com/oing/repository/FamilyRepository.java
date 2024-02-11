package com.oing.repository;

import com.oing.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, String> {
    long countByScoreGreaterThanEqual(int familyScore);

    List<Family> findAllByOrderByScoreDesc();
}
