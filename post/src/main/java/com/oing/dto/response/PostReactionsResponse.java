package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "피드 게시물 이모지 응답")
public record PostReactionsResponse(
        @Schema(description = "이모지1을 누른 사용자 ID 목록", example = "[01HGW2N7EHJVJ4CJ999RRS2E97, 01HGW2N7EHJVJ4CJ999RRS2E97]")
        List<String> emoji1MemberIds,

        @Schema(description = "이모지2를 누른 사용자 ID 목록", example = "[01HGW2N7EHJVJ4CJ999RRS2E97]")
        List<String> emoji2MemberIds,

        @Schema(description = "이모지3을 누른 사용자 ID 목록", example = "[]")
        List<String> emoji3MemberIds,

        @Schema(description = "이모지4를 누른 사용자 ID 목록", example = "[]")
        List<String> emoji4MemberIds,

        @Schema(description = "이모지5를 누른 사용자 ID 목록", example = "[01HGW2N7EHJVJ4CJ999RRS2E97, 01HGW2N7EHJVJ4CJ999RRS2E97]")
        List<String> emoji5MemberIds
) {
}
