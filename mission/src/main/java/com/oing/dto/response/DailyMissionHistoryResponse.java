package com.oing.dto.response;

import com.oing.domain.DailyMissionHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "일별 미션 이력 응답")
public record DailyMissionHistoryResponse(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(description = "날짜", example = "2021-10-01")
        LocalDate date,

        @Schema(description = "미션 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String missionId
) {
    public static DailyMissionHistoryResponse from(DailyMissionHistory dailyMissionHistory) {
        return new DailyMissionHistoryResponse(
                dailyMissionHistory.getDate(),
                dailyMissionHistory.getMission().getId()
        );
    }
}
