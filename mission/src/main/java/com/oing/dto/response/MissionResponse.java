package com.oing.dto.response;

import com.oing.domain.Mission;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미션 응답")
public record MissionResponse(

        @Schema(description = "미션 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String id,

        @Schema(description = "미션 내용", example = "오늘의 기분을 나타내는 사진 찍기.")
        String content
) {
    public static MissionResponse from(Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getContent()
        );
    }
}
