package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/30
 * Time: 12:30 PM
 */
@Schema(description = "피드 게시물 반응 요약")
public record PostReactionSummaryResponse(
        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "피드 게시물 반응 요약", example = "")
        List<PostReactionSummaryResponseElement> results
) {
        @Schema(description = "피드 게시물 반응 요약 내용")
        public static record PostReactionSummaryResponseElement(
                @Schema(description = "반응 종류", example = "EMOJI_1")
                String emojiType,

                @Schema(description = "반응 개수", example = "3")
                int count
        ) {

        }
}
