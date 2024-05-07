package com.oing.repository;

import com.oing.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, String>  {

    @Query(value = "SELECT * FROM mission WHERE mission_id NOT IN :excludingMissionIds ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Mission> findRandomMissionExcludingIds(List<String> excludingMissionIds);
}
