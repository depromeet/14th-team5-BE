package com.oing.service;

import java.time.LocalDate;

public interface MissionBridge {

    String getContentByMissionId(String missionId);

    String getContentByDate(LocalDate date);

    String getTodayMissionId();
}
