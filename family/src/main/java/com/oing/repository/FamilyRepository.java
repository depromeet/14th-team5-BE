package com.oing.repository;

import com.oing.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, String>, FamilyRepositoryCustom {
    List<Family> findAllByOrderByScoreDesc();
}
