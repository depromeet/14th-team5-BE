package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "일일 미션 응답")
public record DailyMissionResponse(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(description = "미션 날짜", example = "2024-04-06")
        LocalDate date,

        @Schema(description = "미션 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String id,

        @Schema(description = "미션 내용", example = "오늘의 기분을 나타내는 사진 찍기.")
        String content
) {
}
