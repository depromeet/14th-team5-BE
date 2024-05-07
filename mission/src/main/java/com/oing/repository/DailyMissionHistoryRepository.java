package com.oing.repository;

import com.oing.domain.DailyMissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyMissionHistoryRepository extends JpaRepository<DailyMissionHistory, LocalDate>, DailyMissionHistoryRepositoryCustom {
}
