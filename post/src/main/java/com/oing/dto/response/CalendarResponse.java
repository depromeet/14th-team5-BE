package com.oing.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "캘린더 응답")
public record CalendarResponse(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @Parameter(description = "오늘의 날짜", example = "2023-12-05")
        LocalDate date,

        @Schema(description = "대표 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String representativePostId,

        @Schema(description = "대표 썸네일 URL", example = "https://j1ansx15683.edge.naverncp.com/image/absc45j/image.jpg?type=f&w=96&h=96")
        String representativeThumbnailUrl
) {
}
