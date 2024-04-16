package com.oing.restapi;

import com.oing.dto.response.DailyMissionResponse;
import com.oing.dto.response.MissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "미션 API", description = "미션 조회 API")
@RestController
@Validated
@RequestMapping("/v1/missions")
public interface MissionApi {

    @Operation(summary = "ID를 통한 미션 조회", description = "미션 ID를 통해 미션 정보르 조회합니다.")
    @GetMapping("/{missionId}")
    MissionResponse getMissionByMissionId(String missionId);

    @Operation(summary = "금일의 일일 미션 조회", description = "오늘 선정된 일일 미션을 조회합니다.")
    @GetMapping("/today")
    DailyMissionResponse getTodayMission();
}
