package com.oing.service;

import com.oing.domain.Mission;
import com.oing.dto.request.CreateMissionRequest;
import com.oing.dto.response.MissionResponse;
import com.oing.exception.MissionNotFoundException;
import com.oing.repository.MissionRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DailyMissionHistoryService dailyMissionHistoryService;

    private final IdentityGenerator identityGenerator;


    public MissionResponse createMission(CreateMissionRequest request) {
        log.info("Create mission request: {}", request.content());

        Mission mission = Mission.builder()
            .id(identityGenerator.generateIdentity())
            .content(request.content())
            .build();
        mission = missionRepository.save(mission);

        return MissionResponse.from(mission);
    }

    public Mission getMissionByMissionId(String missionId) {
        return missionRepository.findById(missionId)
            .orElseThrow(MissionNotFoundException::new);
    }

    public MissionResponse getMissionByDate(LocalDate date) {
        Mission mission = dailyMissionHistoryService.getDailyMissionHistoryByDate(date).getMission();

        return MissionResponse.from(mission);
    }

    public Optional<Mission> findRandomMissionExcludingIds(List<String> excludingMissionIds) {
        return missionRepository.findRandomMissionExcludingIds(excludingMissionIds);
    }

    @Transactional
    public void updateMission(String missionId, CreateMissionRequest request) {
        log.info("Update mission request: {}, {}", missionId, request.content());

        Mission mission = getMissionByMissionId(missionId);
        mission.updateContent(request.content());
    }

    public void deleteMission(String missionId) {
        log.info("Delete mission request: {}", missionId);

        missionRepository.deleteById(missionId);
    }
}
