package com.oing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionBridgeImpl implements MissionBridge {

    private final MissionService missionService;

    @Override
    public String getContentByMissionId(String missionId) {
        return missionService.getMissionByMissionId(missionId).getContent();
    }
}
