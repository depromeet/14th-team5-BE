package com.oing.repository;

import com.oing.domain.FamilyTopPercentageHistory;
import com.oing.domain.FamilyTopPercentageHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyTopPercentageHistoryRepository extends JpaRepository<FamilyTopPercentageHistory, FamilyTopPercentageHistoryId> {
}
