package com.oing.repository;

import com.oing.domain.FamilyTopPercentageHistory;
import com.oing.domain.FamilyTopPercentageHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FamilyTopPercentageHistoryRepository extends JpaRepository<FamilyTopPercentageHistory, FamilyTopPercentageHistoryId> {

    Optional<FamilyTopPercentageHistory> findByFamilyTopPercentageHistoryId(FamilyTopPercentageHistoryId familyTopPercentageHistoryId);
}
