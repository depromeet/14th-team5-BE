package com.oing.dto.response;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "월간 캘린더 응답")
public record MonthlyCalendarResponse(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(description = "오늘의 날짜", example = "2023-12-05")
        LocalDate date,

        @Schema(description = "대표 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String representativePostId,

        @Schema(description = "대표 썸네일 URL", example = "https://j1ansx15683.edge.naverncp.com/image/absc45j/image.jpg?type=f&w=96&h=96")
        String representativeThumbnailUrl,

        @Schema(description = "모든 가족 구성원이 업로드 했는지 여부", example = "true")
        boolean allFamilyMembersUploaded
) {
}
