package com.oing.dto.response;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Schema(description = "일간 캘린더 응답")
public record DailyCalendarResponse(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Parameter(description = "오늘의 날짜", example = "2023-12-05")
        LocalDate date,

        @Schema(description = "게시글 유형", example = "SURVIVAL")
        String type,

        @Schema(description = "게시글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "게시글 사진 url", example = "https://j1ansx15683.edge.naverncp.com/image/absc45j/image.jpg?type=f&w=96&h=96")
        String postImgUrl,

        @Schema(description = "게시물 내용", example = "맛있는 밥!")
        String postContent,

        @Schema(description = "미션 내용 (생존신고 게시글이라면 null 반환)", example = "오늘의 기분을 나타내는 사진 찍기.")
        String missionContent,

        @Schema(description = "게시물 작성자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String authorId,

        @Schema(description = "게시물 댓글 수", example = "3")
        Integer commentCount,

        @Schema(description = "게시물 반응 수", example = "2")
        Integer emojiCount,

        @Schema(description = "모든 가족 구성원이 업로드 했는지 여부", example = "true")
        boolean allFamilyMembersUploaded,

        @Schema(description = "피드 작성 시간", example = "2023-12-23T01:53:21.577347+09:00")
        ZonedDateTime createdAt
) {

        public static DailyCalendarResponse of(PostResponse postResponse, String missionContent, boolean allFamilyMembersUploaded) {
                return new DailyCalendarResponse(
                        postResponse.createdAt().toLocalDate(),
                        postResponse.type(),
                        postResponse.postId(),
                        postResponse.imageUrl(),
                        postResponse.content(),
                        missionContent,
                        postResponse.authorId(),
                        postResponse.commentCount(),
                        postResponse.emojiCount(),
                        allFamilyMembersUploaded,
                        postResponse.createdAt()
                );
        }
}
