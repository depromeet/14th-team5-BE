package com.oing.dto.response;

import com.oing.domain.PostType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "일간 캘린더 응답")
public record DailyCalendarResponse(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(description = "오늘의 날짜", example = "2023-12-05")
        LocalDate date,

        @Schema(description = "게시글 유형", example = "SURVIVAL")
        PostType type,

        @Schema(description = "게시글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "게시글 사진 url", example = "https://j1ansx15683.edge.naverncp.com/image/absc45j/image.jpg?type=f&w=96&h=96")
        String postImgUrl,

        @Schema(description = "미션 내용 (생존신고 게시글이랑 null 반환)", example = "오늘의 기분을 나타내는 사진 찍기.")
        String missionContent,

        @Schema(description = "모든 가족 구성원이 업로드 했는지 여부", example = "true")
        boolean allFamilyMembersUploaded
) {
}
