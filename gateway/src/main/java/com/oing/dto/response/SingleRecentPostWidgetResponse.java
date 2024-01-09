package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "단일 최근 게시물 위젯 응답")
public record SingleRecentPostWidgetResponse(

        @Schema(description = "게시자 이름", example = "권순찬")
        String authorName,

        @Schema(description = "게시자 프로필 사진 주소", example = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97")
        String authorProfileImageUrl,

        @Schema(description = "피드 게시물 사진 주소", example = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97")
        String postImageUrl,

        @Schema(description = "피드 게시물 내용", example = "고양이가귀여워요")
        String postContent
) {
}
