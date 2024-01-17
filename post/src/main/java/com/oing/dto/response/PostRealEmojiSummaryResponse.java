package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "피드 게시물 리얼 이모지 요약")
public record PostRealEmojiSummaryResponse(
        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "피드 게시물 리얼 이모지 요약", example = "")
        List<PostRealEmojiSummaryResponseElement> results
) {
    @Schema(description = "피드 게시물 반응 요약 내용")
    public static record PostRealEmojiSummaryResponseElement(
            @Schema(description = "리얼 이모지 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            String realEmojiId,

            @Schema(description = "반응 개수", example = "3")
            int count
    ) {

    }
}
